package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;
import su.itpro.tasktracker.model.enums.Gender;

@FieldNameConstants
public record ProfileUpdateDto(@NotBlank
                               @Size(min = 2, max = 32)
                               String lastname,

                               @NotBlank
                               @Size(min = 2, max = 32)
                               String firstname,

                               String surname,
                               Gender gender,
                               String aboutMe) {

}
