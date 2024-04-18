package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record AccountUpdateDto(@NotBlank
                               @Email
                               String email,

                               @NotBlank
                               @Size(min = 3, max = 128)
                               @Pattern(regexp = "^[a-zA-Z][-_.a-zA-Z0-9]*$")
                               String username) {

}
