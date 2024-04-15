package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountUpdateDto(@NotBlank
                               @Email
                               String email,

                               @NotBlank
                               @Size(min = 3, max = 128)
                               String username) {

}
