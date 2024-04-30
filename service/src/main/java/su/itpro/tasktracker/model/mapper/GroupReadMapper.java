package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.model.entity.Group;

@Component
public class GroupReadMapper implements Mapper<Group, GroupReadDto> {

  @Override
  public GroupReadDto map(Group group) {
    return GroupReadDto.builder()
        .id(group.getId())
        .name(group.getName())
        .createdAt(group.getCreatedAt())
        .build();
  }
}
