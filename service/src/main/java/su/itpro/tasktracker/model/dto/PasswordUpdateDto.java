package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.experimental.FieldNameConstants;
import su.itpro.tasktracker.validation.PasswordCheckRepeat;

@FieldNameConstants
@PasswordCheckRepeat
public record PasswordUpdateDto(@NotBlank
                                String currentPassword,

                                @NotBlank
                                @Size(min = 4, max = 64)
                                String newPassword,

                                @NotBlank
                                @Size(min = 4, max = 64)

                                String repeatPassword) {

  @Override
  public String toString() {
    return "PasswordUpdateDto{" +
           "currentPassword='****'" +
           ", newPassword='****'" +
           ", repeatPassword='****'" +
           '}';
  }
}
