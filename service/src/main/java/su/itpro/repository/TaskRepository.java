package su.itpro.repository;

import static su.itpro.model.entity.QAccount.account;
import static su.itpro.model.entity.QTask.task;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import java.util.List;
import org.hibernate.graph.GraphSemantic;
import su.itpro.model.dao.QPredicate;
import su.itpro.model.dto.TaskFilter;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Task;

public class TaskRepository extends BaseRepository<Long, Task> {

  public TaskRepository(EntityManager entityManager) {
    super(entityManager, Task.class);
  }

  public List<Task> findAllByFilter(TaskFilter filter) {
    Predicate predicate = QPredicate.builder()
        .add(filter.accountId(), task.assigned.id::eq)
        .add(filter.parentId(), task.parent.id::eq)
        .add(filter.priorities(), task.priority::in)
        .add(filter.statuses(), task.status::in)
        .add(filter.types(), task.type::in)
        .buildAnd();

    EntityGraph<Task> taskGraph = entityManager.createEntityGraph(Task.class);
    taskGraph.addAttributeNodes("project", "category", "parent");
    Subgraph<Account> accountSubgraph = taskGraph.addSubgraph("assigned", Account.class);
    accountSubgraph.addAttributeNodes("profile", "group");

    return new JPAQuery<Task>(entityManager)
        .setHint(GraphSemantic.FETCH.getJakartaHintName(), taskGraph)
        .select(task)
        .from(task)
        .leftJoin(task.assigned, account)
        .where(predicate)
        .fetch();
  }
}
