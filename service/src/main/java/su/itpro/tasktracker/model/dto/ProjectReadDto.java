package su.itpro.tasktracker.model.dto;

import java.time.Instant;
import lombok.Builder;

@Builder
public record ProjectReadDto(Integer id,
                             String name,
                             Instant createdAt,
                             String description) {

}
