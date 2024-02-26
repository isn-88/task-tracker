package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
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
import su.itpro.model.entity.Task_;
import su.itpro.model.enums.TaskPriority;
import su.itpro.model.enums.Role;
import su.itpro.model.enums.TaskStatus;
import su.itpro.util.HibernateTestUtil;

public class TaskCriteriaIT {

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
  void findTaskWithFilter_shouldBeReturnTwoTasks() {
    TaskPriority priority = TaskPriority.NORMAL;
    TaskFilterDto filterDto = TaskFilterDto.builder()
        .statuses(List.of(TaskStatus.ASSIGNED))
        .priorities(List.of(priority))
        .build();

    List<Task> actualResult = findTaskByFilter(filterDto);

    assertThat(actualResult).isNotEmpty();
    assertThat(actualResult).hasSize(2);
    assertThat(actualResult.get(0).getPriority()).isEqualByComparingTo(priority);
    assertThat(actualResult.get(0).getAssigned()).isNotNull();
    assertThat(actualResult.get(1).getPriority()).isEqualByComparingTo(priority);
    assertThat(actualResult.get(1).getAssigned()).isNotNull();
  }

  @Test
  void findTaskWithFilter_shouldBeReturnOneTask() {
    TaskPriority priority = TaskPriority.LOW;
    TaskFilterDto filterDto = TaskFilterDto.builder()
        .statuses(List.of(TaskStatus.NEW))
        .priorities(List.of(priority))
        .build();

    List<Task> actualResult = findTaskByFilter(filterDto);

    assertThat(actualResult).isNotEmpty();
    assertThat(actualResult).hasSize(1);
    assertThat(actualResult.get(0).getPriority()).isEqualByComparingTo(priority);
    assertThat(actualResult.get(0).getAssigned()).isNull();
  }

  private List<Task> findTaskByFilter(TaskFilterDto filter) {
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
    Root<Task> task = criteria.from(Task.class);

    List<Predicate> predicates = new ArrayList<>();
    if (filter.priorities() != null && !filter.priorities().isEmpty()) {
      predicates.add(task.get(Task_.PRIORITY).in(filter.priorities()));
    }
    if (filter.statuses() != null && !filter.statuses().isEmpty()) {
      predicates.add(task.get(Task_.STATUS).in(filter.statuses()));
    }
    if (filter.types() != null && !filter.types().isEmpty()) {
      predicates.add(task.get(Task_.TYPE).in(filter.types()));
    }

    RootGraph<Task> taskGraph = session.createEntityGraph(Task.class);
    taskGraph.addAttributeNodes("project", "category", "parent");
    SubGraph<Account> accountSubgraph = taskGraph.addSubgraph("assigned", Account.class);
    accountSubgraph.addAttributeNodes("profile", "group");

    criteria.select(task)
        .where(predicates.toArray(Predicate[]::new));

    return session.createQuery(criteria)
        .setHint(GraphSemantic.LOAD.getJakartaHintName(), taskGraph)
        .list();
  }
}
