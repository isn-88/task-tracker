package su.itpro.tasktracker.integration.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestSecurity;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class TaskControllerIT extends IntegrationTestSecurity {

  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  private Integer savedProjectId;
  private Task parentTask;
  private Task childTask;

  @BeforeEach
  void prepare() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    entityManager.persist(project);
    savedProjectId = project.getId();
    parentTask = Task.builder()
        .title("Task first")
        .project(project)
        .type(TaskType.FEATURE)
        .status(TaskStatus.ASSIGNED)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(parentTask);
    childTask = Task.builder()
        .parent(parentTask)
        .title("Task second")
        .project(project)
        .type(TaskType.FEATURE)
        .status(TaskStatus.ASSIGNED)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(childTask);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void findAll() throws Exception {
    mockMvc.perform(get("/api/v1/tasks").param("findPattern", "task"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.success").value(true),
            jsonPath("$.length()").value(2)
        );
  }

}