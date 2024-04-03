package su.itpro.tasktracker.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.TaskReadDto;
import su.itpro.tasktracker.model.entity.Task;

@Component
@RequiredArgsConstructor
public class TaskReadMapper implements Mapper<Task, TaskReadDto> {

  private final ProjectReadMapper projectReadMapper;
  private final TaskAssignedMapper taskAssignedMapper;
  private final TaskParentMapper taskParentMapper;

  @Override
  public TaskReadDto map(Task task) {
    return TaskReadDto.builder()
        .id(task.getId())
        .parentTask(taskParentMapper.map(task.getParent()))
        .project(projectReadMapper.map(task.getProject()))
        .title(task.getTitle())
        .type(task.getType())
        .status(task.getStatus())
        .priority(task.getPriority())
        .assigned(taskAssignedMapper.map(task))
        .category(task.getCategory())
        .createAt(task.getCreateAt())
        .closeAt(task.getCloseAt())
        .progress(task.getProgress())
        .description(task.getDescription())
        .build();
  }
}
