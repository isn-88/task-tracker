package su.itpro.tasktracker.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.integration.IntegrationTestBase;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.dto.TaskCreateUpdateDto;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.dto.TaskReadDto;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;
import su.itpro.tasktracker.service.TaskService;

@RequiredArgsConstructor
class TaskServiceIT extends IntegrationTestBase {

  private final TaskService taskService;
  private final EntityManager entityManager;

  private Task savedTask;
  private Integer savedProjectId;

  @BeforeEach
  void prepare() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    entityManager.persist(project);
    savedProjectId = project.getId();
    Task parentTask = Task.builder()
        .title("parent-1")
        .project(project)
        .type(TaskType.FEATURE)
        .status(TaskStatus.ASSIGNED)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(parentTask);
    savedTask = parentTask;
    entityManager.flush();
    entityManager.clear();
  }
  @Test
  void findAllByFilter() {
    TaskFilter filter = TaskFilter.builder()
        .statuses(List.of(TaskStatus.ASSIGNED))
        .build();

    List<TaskReadDto> actualResult = taskService.findAllBy(filter);
    assertThat(actualResult).hasSize(1);
  }

  @Test
  void findById() {
    Optional<TaskReadDto> actualResult = taskService.findById(savedTask.getId());
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().id()).isEqualTo(savedTask.getId());
  }

  @Test
  void create() {
    String title = "Title";
    String description = "Description";
    TaskCreateUpdateDto taskDto = TaskCreateUpdateDto.builder()
        .projectId(savedProjectId)
        .type("FEATURE")
        .status("NEW")
        .priority("NORMAL")
        .title(title)
        .description(description)
        .build();

    TaskReadDto actualResult = taskService.create(taskDto);
    assertAll(() -> {
      assertThat(actualResult.id()).isNotNull();
      assertThat(actualResult.parentTask()).isNull();
      assertThat(actualResult.project().id()).isEqualTo(savedProjectId);
      assertThat(actualResult.type()).isEqualTo(TaskType.FEATURE);
      assertThat(actualResult.title()).isEqualTo(title);
      assertThat(actualResult.status()).isEqualTo(TaskStatus.NEW);
      assertThat(actualResult.priority()).isEqualTo(TaskPriority.NORMAL);
      assertThat(actualResult.assigned()).isEqualTo(TaskAssignedDto.builder().build());
      assertThat(actualResult.category()).isNull();
      assertThat(actualResult.closeAt()).isNull();
      assertThat(actualResult.description()).isEqualTo(description);
    });
  }

  @Test
  void update() {
    String title = "Title updated";
    String description = "Description updated";
    TaskCreateUpdateDto taskUpdateDto = TaskCreateUpdateDto.builder()
        .projectId(savedProjectId)
        .type("FEATURE")
        .status("ASSIGNED")
        .title(title)
        .priority("HIGH")
        .description(description)
        .build();

    Optional<TaskReadDto> actualResult = taskService.update(savedTask.getId(), taskUpdateDto);
    assertThat(actualResult).isPresent();
    actualResult.ifPresent(updatedTask ->
      assertAll(() -> {
        assertThat(updatedTask.id()).isEqualTo(savedTask.getId());
        assertThat(updatedTask.parentTask()).isNull();
        assertThat(updatedTask.project().id()).isEqualTo(savedProjectId);
        assertThat(updatedTask.type()).isEqualTo(TaskType.FEATURE);
        assertThat(updatedTask.title()).isEqualTo(title);
        assertThat(updatedTask.status()).isEqualTo(TaskStatus.ASSIGNED);
        assertThat(updatedTask.priority()).isEqualTo(TaskPriority.HIGH);
        assertThat(updatedTask.category()).isNull();
        assertThat(updatedTask.closeAt()).isNull();
        assertThat(updatedTask.description()).isEqualTo(description);
      }));
  }

}