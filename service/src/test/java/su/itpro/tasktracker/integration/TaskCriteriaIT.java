package su.itpro.tasktracker.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.model.dao.CPredicate;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.model.entity.Task_;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.model.enums.TaskStatus;

@RequiredArgsConstructor
public class TaskCriteriaIT extends IntegrationTestBase {

  private final EntityManager entityManager;

  private List<Task> normalTasks;
  private List<Task> lowTasks;
  private Task parentTask;

  @BeforeEach
  void prepare() {
    Project project = Project.builder()
        .name("TestProject")
        .build();
    entityManager.persist(project);
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
        .project(project)
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithTwoTasks)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(parentTask);
    Task childTask1 = Task.builder()
        .title("child-1")
        .project(project)
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
        .project(project)
        .parent(parentTask)
        .status(TaskStatus.ASSIGNED)
        .assigned(accountWithOneTask)
        .priority(TaskPriority.NORMAL)
        .build();
    entityManager.persist(childTask2);

    Task freeTask = Task.builder()
        .title("freeTask")
        .project(project)
        .status(TaskStatus.NEW)
        .priority(TaskPriority.LOW)
        .build();
    entityManager.persist(freeTask);

    this.parentTask = parentTask;
    normalTasks = List.of(childTask2);
    lowTasks = List.of(freeTask);
    entityManager.flush();
    entityManager.clear();
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

    assertThat(actualResult).hasSize(1);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(normalTasks);
  }

  @Test
  void findTaskWithFilter_shouldBeReturnTasksWithLowPriority() {
    TaskFilter filterDto = TaskFilter.builder()
        .statuses(List.of(TaskStatus.NEW))
        .priorities(List.of(TaskPriority.LOW))
        .build();

    List<Task> actualResult = findTaskByFilter(filterDto);

    assertThat(actualResult).hasSize(1);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(lowTasks);
  }

  private List<Task> findTaskByFilter(TaskFilter filter) {
    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    CriteriaQuery<Task> criteria = cb.createQuery(Task.class);
    Root<Task> task = criteria.from(Task.class);

    Predicate[] predicates = CPredicate.builder()
        .add(filter.parentId(), parentId -> cb.equal(task.get(Task_.PARENT).get(Task_.ID), parentId))
        .add(filter.priorities(), task.get(Task_.PRIORITY)::in)
        .add(filter.statuses(), task.get(Task_.STATUS)::in)
        .build();

    EntityGraph<Task> taskGraph = entityManager.createEntityGraph(Task.class);
    taskGraph.addAttributeNodes("project", "category", "parent");
    Subgraph<Account> accountSubgraph = taskGraph.addSubgraph("assigned", Account.class);
    accountSubgraph.addAttributeNodes("profile", "group");

    criteria.where(predicates);
    criteria.select(task);

    return entityManager.createQuery(criteria)
        .setHint(GraphSemantic.FETCH.getJakartaHintName(), taskGraph)
        .getResultList();
  }
}
