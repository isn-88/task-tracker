package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.GroupUpdateDto;
import su.itpro.tasktracker.model.entity.Group;

@Component
public class GroupCreateMapper implements Mapper<GroupUpdateDto, Group> {

  @Override
  public Group map(GroupUpdateDto createDto) {
    return Group.builder()
        .name(createDto.name())
        .build();
  }
}
