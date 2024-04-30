package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.GroupCreateDto;
import su.itpro.tasktracker.model.entity.Group;

@Component
public class GroupCreateMapper implements Mapper<GroupCreateDto, Group> {

  @Override
  public Group map(GroupCreateDto createDto) {
    return Group.builder()
        .name(createDto.name())
        .build();
  }
}
