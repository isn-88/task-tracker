package su.itpro.tasktracker.repository.impl;

import static su.itpro.tasktracker.model.entity.QTask.task;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import su.itpro.tasktracker.model.dao.QPredicate;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.repository.TaskFilterRepository;

@RequiredArgsConstructor
public class TaskFilterRepositoryImpl implements TaskFilterRepository {

  private final EntityManager entityManager;

  public List<Task> findAllByFilter(TaskFilter filter) {
    Predicate predicate = QPredicate.builder()
        .add(filter.findPattern(), task.title::containsIgnoreCase)
        .add(filter.parentId(), task.parent.id::eq)
        .add(filter.ownerId(), task.owner.id::eq)
        .add(filter.priorities(), task.priority::in)
        .add(filter.statuses(), task.status::in)
        .add(filter.types(), task.type::in)
        .add(filter.assignedAccountId(), task.assignedAccount.id::in)
        .add(filter.assignedGroupId(), task.assignedGroup.id::in)
        .buildAnd();

    EntityGraph<Task> taskGraph = entityManager.createEntityGraph(Task.class);
    taskGraph.addAttributeNodes("parent", "project", "category");

    return new JPAQuery<Task>(entityManager)
        .setHint(GraphSemantic.FETCH.getJakartaHintName(), taskGraph)
        .select(task)
        .from(task)
        .where(predicate)
        .fetch();
  }
}
