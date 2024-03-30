package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.enums.AssignedType;

@Component
public class AssignedGroupMapper implements Mapper<Group, TaskAssignedDto> {

  @Override
  public TaskAssignedDto map(Group group) {
    if (group == null) {
      return null;
    }

    return TaskAssignedDto.builder()
        .id(group.getId())
        .name(group.getName())
        .type(AssignedType.G)
        .build();
  }
}
