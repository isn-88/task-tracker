package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static su.itpro.model.entity.QAccount.account;
import static su.itpro.model.entity.QGroup.group;
import static su.itpro.model.entity.QTask.task;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.dto.TaskFilterDto;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Group;
import su.itpro.model.entity.Profile;
import su.itpro.model.entity.Task;
import su.itpro.model.enums.Role;
import su.itpro.model.enums.TaskPriority;
import su.itpro.model.enums.TaskStatus;
import su.itpro.util.HibernateTestUtil;

public class TaskQuerydslIT {

  private static SessionFactory sessionFactory;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

  @BeforeEach
  void prepare() {
    session = sessionFactory.openSession();
    session.beginTransaction();

    Group group = Group.builder()
        .name("General")
        .build();
    session.persist(group);
    Account accountWithTwoTasks = Account.builder()
        .email("account-1@email.com")
        .login("accountWithTwoTasks")
        .password("password")
        .role(Role.USER)
        .group(group)
        .build();
    session.persist(accountWithTwoTasks);
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
    accountWithTwoTasks.getTasks().add(parentTask);
    session.persist(parentTask);
    Task childTask1 = Task.builder()
        .title("child-1")
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.NORMAL)
        .build();
    accountWithTwoTasks.getTasks().add(childTask1);
    session.persist(childTask1);

    Account accountWithOneTask = Account.builder()
        .email("account-2@email.com")
        .login("accountWithOneTask")
        .password("password")
        .role(Role.USER)
        .build();
    session.persist(accountWithOneTask);
    Task childTask2 = Task.builder()
        .title("child-2")
        .parent(parentTask)
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithOneTask)
        .priority(TaskPriority.NORMAL)
        .build();
    accountWithOneTask.getTasks().add(childTask2);
    session.persist(childTask2);

    Task freeTask = Task.builder()
        .title("freeTask")
        .status(TaskStatus.NEW)
        .priority(TaskPriority.LOW)
        .build();
    session.persist(freeTask);

    session.flush();
    session.clear();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
    session.close();
  }

  @Test
  void findAccountsWithTaskFilter_shouldBeFindOneAccountWithOneTask() {
    TaskPriority priority = TaskPriority.HIGH;
    TaskFilterDto filter = TaskFilterDto.builder()
        .statuses(List.of(TaskStatus.ASSIGNED))
        .priorities(List.of(priority))
        .build();

    List<Account> actualResult = findAccountWithTasksByFilter(filter);

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).hasSize(1);
    assertThat(actualResult.get(0).getProfile()).isNotNull();
    assertThat(actualResult.get(0).getGroup()).isNotNull();
    assertThat(actualResult.get(0).getTasks()).isNotNull();
    assertThat(actualResult.get(0).getTasks()).hasSize(1);
    assertThat(actualResult.get(0).getTasks().get(0).getPriority()).isEqualByComparingTo(priority);
  }

  @Test
  void findAccountsWithTaskFilter_mustBeEmpty() {
    TaskFilterDto filter = TaskFilterDto.builder()
        .statuses(List.of(TaskStatus.NEW))
        .priorities(List.of(TaskPriority.LOW))
        .build();

    List<Account> actualResult = findAccountWithTasksByFilter(filter);

    assertThat(actualResult).isEmpty();
  }

  private List<Account> findAccountWithTasksByFilter(TaskFilterDto filter) {
    List<Predicate> predicates = new ArrayList<>();
    if (filter.parentId() != null) {
      predicates.add(task.parent.id.eq(filter.parentId()));
    }
    if (filter.priorities() != null && !filter.priorities().isEmpty()) {
      predicates.add(task.priority.in(filter.priorities()));
    }
    if (filter.statuses() != null && !filter.statuses().isEmpty()) {
      predicates.add(task.status.in(filter.statuses()));
    }
    if (filter.types() != null && !filter.types().isEmpty()) {
      predicates.add(task.type.in(filter.types()));
    }

    RootGraph<Account> accountGraph = session.createEntityGraph(Account.class);
    accountGraph.addAttributeNodes("profile");
    accountGraph.addAttributeNodes("group");
    accountGraph.addAttributeNodes("tasks");

    return new JPAQuery<Account>(session)
        .setHint(GraphSemantic.LOAD.getJakartaHintName(), accountGraph)
        .select(account)
        .from(account)
        .leftJoin(account.profile)
        .leftJoin(account.group, group)
        .leftJoin(account.tasks, task)
        .where(predicates.toArray(Predicate[]::new))
        .fetch();
  }
}
