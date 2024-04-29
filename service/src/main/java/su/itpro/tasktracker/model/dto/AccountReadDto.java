package su.itpro.tasktracker.model.dto;

import lombok.Builder;
import su.itpro.tasktracker.model.enums.Gender;
import su.itpro.tasktracker.model.enums.Role;

@Builder
public record AccountReadDto(Long id,
                             String email,
                             String username,
                             Role role,
                             Boolean isEnabled,
                             String lastname,
                             String firstname,
                             String surname,
                             String fullName,
                             Gender gender,
                             String aboutMe) {

}
