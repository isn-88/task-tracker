package su.itpro.tasktracker.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import su.itpro.tasktracker.model.dto.PasswordUpdateDto;

public class PasswordCheckRepeatValidator implements
    ConstraintValidator<PasswordCheckRepeat, PasswordUpdateDto> {

  @Override
  public boolean isValid(PasswordUpdateDto dto,
                         ConstraintValidatorContext constraintValidatorContext) {
    return dto.repeatPassword().equals(dto.newPassword());
  }
}
