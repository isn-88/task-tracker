package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import su.itpro.util.HibernateTestUtil;

public class TaskIT {

  private static SessionFactory sessionFactory;

  private static AccountRepository accountRepository;

  private static GroupRepository groupRepository;

  private static TaskRepository taskRepository;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
    Session proxySession = HibernateTestUtil.buildProxySession(sessionFactory);
    accountRepository = new AccountRepository(proxySession);
    groupRepository = new GroupRepository(proxySession);
    taskRepository = new TaskRepository(proxySession);
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

  @BeforeEach
  void prepare() {
    session = sessionFactory.getCurrentSession();
    session.beginTransaction();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
  }

  @Test
  void createTask() {
    Task task = Task.builder()
        .title("title-create")
        .status(TaskStatus.NEW)
        .priority(TaskPriority.NORMAL)
        .build();
    taskRepository.save(task);
    session.flush();
    session.clear();

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
    session.flush();
    session.clear();

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
    session.flush();
    session.clear();

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
    session.flush();
    session.clear();
    task.setTitle("updated");
    taskRepository.update(task);
    session.flush();
    session.clear();

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
    session.flush();

    taskRepository.delete(task);
    session.flush();

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
    session.flush();
    session.clear();
    TaskFilter filter = TaskFilter.builder()
        .accountId(accountWithTwoTasks.getId())
        .priorities(List.of(TaskPriority.HIGH))
        .build();

    List<Task> actualResult = taskRepository.findAllByFilter(filter);

    assertThat(actualResult).hasSize(1);
    assertThat(actualResult.get(0)).isEqualTo(parentTask);
  }

}
