package su.itpro.tasktracker.model.dto;

import java.util.List;
import lombok.Builder;
import su.itpro.tasktracker.model.enums.Role;

@Builder
public record AccountFilterDto(String username,
                               String email,
                               Role role,
                               Boolean isEnabled,
                               List<String> fullNameParts) {

}
