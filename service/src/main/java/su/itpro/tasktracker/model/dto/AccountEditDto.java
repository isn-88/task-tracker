package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import su.itpro.tasktracker.model.enums.Role;

public record AccountEditDto(@NotNull
                             Long id,

                             @NotBlank
                             @Email
                             String email,

                             @NotBlank
                             @Size(min = 3, max = 128)
                             @Pattern(regexp = "^[a-zA-Z][-_.a-zA-Z0-9]*$")
                             String username,

                             @NotBlank
                             @Size(min = 2, max = 32)
                             String lastname,

                             @NotBlank
                             @Size(min = 2, max = 32)
                             String firstname,

                             String surname,

                             @NotNull
                             Role role) {

}
