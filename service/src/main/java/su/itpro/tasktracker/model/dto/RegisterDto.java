package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import su.itpro.tasktracker.model.enums.Role;

@Getter
@Setter
@Builder
@FieldNameConstants
public class RegisterDto {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 3, max = 128)
  @Pattern(regexp = "^[a-zA-Z][-_.a-zA-Z0-9]*$", message = "{validation.username.pattern}")
  private String username;

  @NotBlank
  @Size(min = 4, max = 64)
  private String password;

  private Role role;

  @NotBlank
  @Size(min = 2, max = 32)
  private String lastname;

  @NotBlank
  @Size(min = 2, max = 32)
  private String firstname;

}
