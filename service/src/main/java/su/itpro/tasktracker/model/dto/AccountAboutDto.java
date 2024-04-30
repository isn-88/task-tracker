package su.itpro.tasktracker.model.dto;

import java.time.Instant;
import lombok.Builder;
import su.itpro.tasktracker.model.enums.Role;

@Builder
public record AccountAboutDto(Long id,
                              String username,
                              String email,
                              String fullName,
                              Role role,
                              Instant createdAt,
                              Instant lastLoginAt) {

}
