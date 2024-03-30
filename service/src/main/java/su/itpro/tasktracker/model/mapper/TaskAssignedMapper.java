package su.itpro.tasktracker.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.entity.Task;

@Component
@RequiredArgsConstructor
public class TaskAssignedMapper implements Mapper<Task, TaskAssignedDto> {

  private final AssignedAccountMapper assignedAccountMapper;
  private final AssignedGroupMapper assignedGroupMapper;

  @Override
  public TaskAssignedDto map(Task task) {
    if (task.getAssignedAccount() != null) {
      return assignedAccountMapper.map(task.getAssignedAccount());
    } else if (task.getAssignedGroup() != null) {
      return assignedGroupMapper.map(task.getAssignedGroup());
    } else {
      return TaskAssignedDto.builder()
          .build();
    }
  }

}
