package su.itpro.tasktracker.model.dto;

import lombok.Builder;
import su.itpro.tasktracker.model.enums.Gender;

@Builder
public record AccountReadDto(Long id,
                             String email,
                             String username,
                             String lastname,
                             String firstname,
                             String surname,
                             Gender gender,
                             String aboutMe) {

}
