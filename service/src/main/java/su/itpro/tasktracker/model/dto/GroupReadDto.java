package su.itpro.tasktracker.model.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record GroupReadDto(Integer id,
                           String name,
                           Instant createdAt,
                           Long count) {

}
