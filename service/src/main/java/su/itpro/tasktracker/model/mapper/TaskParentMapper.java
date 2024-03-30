package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.TaskParentDto;
import su.itpro.tasktracker.model.entity.Task;

@Component
public class TaskParentMapper implements Mapper<Task, TaskParentDto> {

  @Override
  public TaskParentDto map(Task task) {
    if (task == null) {
      return null;
    }

    return TaskParentDto.builder()
        .id(task.getId())
        .type(task.getType())
        .title(task.getTitle())
        .build();
  }
}
