package su.itpro.tasktracker.model.mapper;

import java.time.Instant;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.GroupReadDto;

@Component
public class GroupCountMapper implements Mapper<Object[], GroupReadDto> {

  @Override
  public GroupReadDto map(Object[] object) {

    return GroupReadDto.builder()
        .id((Integer) object[0])
        .name((String) object[1])
        .createdAt((Instant) object[2])
        .count((Long) object[3])
        .build();
  }
}
