package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.graph.GraphSemantic;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.dao.CPredicate;
import su.itpro.model.dto.TaskFilter;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Group;
import su.itpro.model.entity.Profile;
import su.itpro.model.entity.Task;
import su.itpro.model.entity.Task_;
import su.itpro.model.enums.TaskPriority;
import su.itpro.model.enums.Role;
import su.itpro.model.enums.TaskStatus;

public class TaskCriteriaIT extends IntegrationBase {

  private List<Task> normalTasks;

  private List<Task> lowTasks;

  private Task parentTask;

  @BeforeEach
  void prepare() {
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
    session.persist(parentTask);
    Task childTask1 = Task.builder()
        .title("child-1")
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.NORMAL)
        .build();
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
    session.persist(childTask2);

    Task freeTask = Task.builder()
        .title("freeTask")
        .status(TaskStatus.NEW)
        .priority(TaskPriority.LOW)
        .build();
    session.persist(freeTask);

    this.parentTask = parentTask;
    normalTasks = List.of(childTask1, childTask2);
    lowTasks = List.of(freeTask);
    session.flush();
    session.clear();
  }


  @Test
  void findTaskWithFilter_shouldBeReturnTasksWithNormalPriority() {
    TaskPriority filterPriority = TaskPriority.NORMAL;
    TaskFilter filterDto = TaskFilter.builder()
        .parentId(parentTask.getId())
        .statuses(List.of(TaskStatus.ASSIGNED))
        .priorities(List.of(filterPriority))
        .build();

    List<Task> actualResult = findTaskByFilter(filterDto);

    assertThat(actualResult).hasSize(2);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(normalTasks);
  }

  @Test
  void findTaskWithFilter_shouldBeReturnTasksWithLowPriority() {
    TaskPriority filterPriority = TaskPriority.LOW;
    TaskFilter filterDto = TaskFilter.builder()
        .statuses(List.of(TaskStatus.NEW))
        .priorities(List.of(filterPriority))
        .build();

    List<Task> actualResult = findTaskByFilter(filterDto);

    assertThat(actualResult).hasSize(1);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(lowTasks);
  }

  private List<Task> findTaskByFilter(TaskFilter filter) {
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
    Root<Task> task = criteria.from(Task.class);

    Predicate predicates = CPredicate.builder(cb)
        .add(filter.priorities(), task.get(Task_.PRIORITY)::in)
        .add(filter.statuses(), task.get(Task_.STATUS)::in)
        .add(filter.types(), task.get(Task_.TYPE)::in)
        .buildAnd();

    RootGraph<Task> taskGraph = session.createEntityGraph(Task.class);
    taskGraph.addAttributeNodes("project", "category", "parent");
    SubGraph<Account> accountSubgraph = taskGraph.addSubgraph("assigned", Account.class);
    accountSubgraph.addAttributeNodes("profile", "group");

    criteria.select(task)
        .where(predicates);

    return session.createQuery(criteria)
        .setHint(GraphSemantic.FETCH.getJakartaHintName(), taskGraph)
        .list();
  }
}
