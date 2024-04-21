package su.itpro.tasktracker.validation;

import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.email;
import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.username;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import su.itpro.tasktracker.model.dto.RegisterDto;
import su.itpro.tasktracker.repository.AccountRepository;

@Component
@RequiredArgsConstructor
public class RegisterPostValidator {

  private final AccountRepository accountRepository;
  private final MessageSource messageSource;

  public void validate(RegisterDto dto, BindingResult bindingResult, Locale locale) {
    if (!bindingResult.hasFieldErrors(email) &&
        accountRepository.existsByEmail(dto.getEmail())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), email, dto.getEmail(), true,
                         new String[]{""}, new Object[]{},
                         messageSource.getMessage("validation.email.exists", null, locale)
          ));
    }
    if (!bindingResult.hasFieldErrors(username) &&
        accountRepository.existsByUsername(dto.getUsername())) {
      bindingResult.addError(
          new FieldError(bindingResult.getObjectName(), username, dto.getUsername(), true,
                         new String[]{""}, new Object[]{},
                         messageSource.getMessage("validation.username.exists", null, locale)
          ));
    }
  }

}
