package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static su.itpro.model.entity.QAccount.account;
import static su.itpro.model.entity.QTask.task;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import java.util.List;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.dao.QPredicate;
import su.itpro.model.dto.TaskFilter;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Group;
import su.itpro.model.entity.Profile;
import su.itpro.model.entity.Task;
import su.itpro.model.enums.Role;
import su.itpro.model.enums.TaskPriority;
import su.itpro.model.enums.TaskStatus;

public class TaskQuerydslIT extends IntegrationBase {

  private Task parentTask;

  private List<Task> normalTasks;

  @BeforeEach
  void prepare() {
    entityManager.getTransaction().begin();

    Group group = Group.builder()
        .name("General")
        .build();
    entityManager.persist(group);
    Account accountWithTwoTasks = Account.builder()
        .email("account-1@email.com")
        .login("accountWithTwoTasks")
        .password("password")
        .role(Role.USER)
        .group(group)
        .build();
    entityManager.persist(accountWithTwoTasks);
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
    entityManager.persist(parentTask);
    Task childTask1 = Task.builder()
        .title("child-1")
        .parent(parentTask)
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.NORMAL)
        .build();
    entityManager.persist(childTask1);

    Account accountWithOneTask = Account.builder()
        .email("account-2@email.com")
        .login("accountWithOneTask")
        .password("password")
        .role(Role.USER)
        .build();
    entityManager.persist(accountWithOneTask);
    Task childTask2 = Task.builder()
        .title("child-2")
        .parent(parentTask)
        .status(TaskStatus.CLOSED)
        .assigned(accountWithOneTask)
        .priority(TaskPriority.NORMAL)
        .build();
    entityManager.persist(childTask2);

    Task freeTask = Task.builder()
        .title("freeTask")
        .status(TaskStatus.NEW)
        .priority(TaskPriority.LOW)
        .build();
    entityManager.persist(freeTask);

    this.parentTask = parentTask;
    normalTasks = List.of(childTask1, childTask2);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void findAccountsWithTaskFilter_shouldBeFindOneAccountWithOneTask() {
    TaskFilter filter = TaskFilter.builder()
        .parentId(parentTask.getId())
        .statuses(List.of(TaskStatus.ASSIGNED, TaskStatus.CLOSED))
        .priorities(List.of(TaskPriority.NORMAL))
        .build();

    List<Task> actualResult = findAccountWithTasksByFilter(filter);

    assertThat(actualResult).hasSize(2);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(normalTasks);
  }

  @Test
  void findAccountsWithTaskFilter_mustBeEmpty() {
    TaskFilter filter = TaskFilter.builder()
        .statuses(List.of(TaskStatus.CLOSED))
        .priorities(List.of(TaskPriority.LOW))
        .build();

    List<Task> actualResult = findAccountWithTasksByFilter(filter);

    assertThat(actualResult).isEmpty();
  }

  private List<Task> findAccountWithTasksByFilter(TaskFilter filter) {
    Predicate predicate = QPredicate.builder()
        .add(filter.parentId(), task.parent.id::eq)
        .add(filter.priorities(), task.priority::in)
        .add(filter.statuses(), task.status::in)
        .add(filter.types(), task.type::in)
        .buildAnd();

    EntityGraph<Task> taskGraph = entityManager.createEntityGraph(Task.class);
    taskGraph.addAttributeNodes("project", "category", "parent");
    Subgraph<Account> accountSubgraph = taskGraph.addSubgraph("assigned", Account.class);
    accountSubgraph.addAttributeNodes("profile", "group");

    return new JPAQuery<Account>(entityManager)
        .setHint(GraphSemantic.FETCH.getJakartaHintName(), taskGraph)
        .select(task)
        .from(task)
        .leftJoin(task.assigned, account)
        .where(predicate)
        .fetch();
  }
}
