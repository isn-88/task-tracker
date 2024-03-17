package su.itpro.tasktracker.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.GroupRepository;
import su.itpro.tasktracker.repository.ProjectRepository;
import su.itpro.tasktracker.repository.TaskRepository;

@RequiredArgsConstructor
public class TaskIT extends IntegrationTestBase {

  private final AccountRepository accountRepository;
  private final GroupRepository groupRepository;
  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;
  private final EntityManager entityManager;

  @Test
  void createTask() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    projectRepository.save(project);
    Task task = Task.builder()
        .title("title-create")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(task);
    entityManager.flush();
    entityManager.clear();

    Optional<Task> actualResult = taskRepository.findById(task.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(task);
  }

  @Test
  void readExistsTask() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    projectRepository.save(project);
    Task task1 = Task.builder()
        .title("title-exist-1")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    Task task2 = Task.builder()
        .title("title-exist-2")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(task1);
    taskRepository.save(task2);
    entityManager.flush();
    entityManager.clear();

    Optional<Task> actualResult = taskRepository.findById(task1.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(task1);
  }

  @Test
  void readNotExistsTask() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    projectRepository.save(project);
    Task task = Task.builder()
        .title("title-not-exist")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(task);
    entityManager.flush();
    entityManager.clear();

    Optional<Task> actualResult = taskRepository.findById(Long.MAX_VALUE);

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateTask() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    projectRepository.save(project);
    Task task = Task.builder()
        .title("title-update")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(task);
    entityManager.flush();
    entityManager.clear();
    task.setTitle("updated");
    taskRepository.update(task);
    entityManager.flush();
    entityManager.clear();

    Optional<Task> actualResult = taskRepository.findById(task.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(task);
  }

  @Test
  void deleteTask() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    projectRepository.save(project);
    Task task = Task.builder()
        .title("title-delete")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(task);
    entityManager.flush();

    taskRepository.delete(task);
    entityManager.flush();

    Optional<Task> actualResult = taskRepository.findById(task.getId());
    assertThat(actualResult).isEmpty();
  }

  @Test
  void findTasksByFilter() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    projectRepository.save(project);
    Group group = Group.builder()
        .name("General")
        .build();
    groupRepository.save(group);
    Account accountWithTwoTasks = Account.builder()
        .email("account-1@email.com")
        .login("accountWithTwoTasks")
        .password("password")
        .role(Role.USER)
        .group(group)
        .build();
    accountRepository.save(accountWithTwoTasks);
    Profile profile = Profile.builder()
        .firstname("Firstname")
        .lastname("Lastname")
        .build();
    profile.setAccount(accountWithTwoTasks);
    Task parentTask = Task.builder()
        .title("parent-1")
        .project(project)
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.HIGH)
        .build();
    taskRepository.save(parentTask);
    Task childTask1 = Task.builder()
        .title("child-1")
        .project(project)
        .parent(parentTask)
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(childTask1);
    Account accountWithOneTask = Account.builder()
        .email("account-2@email.com")
        .login("accountWithOneTask")
        .password("password")
        .role(Role.USER)
        .build();
    accountRepository.save(accountWithOneTask);
    Task childTask2 = Task.builder()
        .title("child-2")
        .project(project)
        .parent(parentTask)
        .status(TaskStatus.CLOSED)
        .assigned(accountWithOneTask)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(childTask2);
    Task freeTask = Task.builder()
        .title("freeTask")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.LOW)
        .build();
    taskRepository.save(freeTask);
    entityManager.flush();
    entityManager.clear();
    TaskFilter filter = TaskFilter.builder()
        .accountId(accountWithTwoTasks.getId())
        .priorities(List.of(TaskPriority.HIGH))
        .build();

    List<Task> actualResult = taskRepository.findAllByFilter(filter);

    assertThat(actualResult).hasSize(1);
    assertThat(actualResult.get(0)).isEqualTo(parentTask);
  }

}
