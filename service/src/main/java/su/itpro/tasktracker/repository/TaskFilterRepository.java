package su.itpro.tasktracker.repository;

import java.util.List;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.entity.Task;

public interface TaskFilterRepository {

  List<Task> findAllByFilter(TaskFilter filter);

}
