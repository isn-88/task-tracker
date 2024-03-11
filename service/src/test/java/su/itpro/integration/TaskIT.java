package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import su.itpro.model.dto.TaskFilter;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Group;
import su.itpro.model.entity.Profile;
import su.itpro.model.entity.Task;
import su.itpro.model.enums.Role;
import su.itpro.model.enums.TaskPriority;
import su.itpro.model.enums.TaskStatus;
import su.itpro.repository.AccountRepository;
import su.itpro.repository.GroupRepository;
import su.itpro.repository.TaskRepository;

public class TaskIT extends IntegrationBase {

  private final AccountRepository accountRepository;

  private final GroupRepository groupRepository;

  private final TaskRepository taskRepository;

  public TaskIT() {
    accountRepository = context.getBean(AccountRepository.class);
    groupRepository = context.getBean(GroupRepository.class);
    taskRepository = context.getBean(TaskRepository.class);
  }

  @Test
  void createTask() {
    Task task = Task.builder()
        .title("title-create")
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
    Task task1 = Task.builder()
        .title("title-exist-1")
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    Task task2 = Task.builder()
        .title("title-exist-2")
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
    Task task = Task.builder()
        .title("title-not-exist")
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
    Task task = Task.builder()
        .title("title-update")
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
    Task task = Task.builder()
        .title("title-delete")
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
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.HIGH)
        .build();
    taskRepository.save(parentTask);
    Task childTask1 = Task.builder()
        .title("child-1")
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
        .parent(parentTask)
        .status(TaskStatus.CLOSED)
        .assigned(accountWithOneTask)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(childTask2);
    Task freeTask = Task.builder()
        .title("freeTask")
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
