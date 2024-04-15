package su.itpro.tasktracker.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.RegistrationDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.enums.Role;

@Component
@RequiredArgsConstructor
public class AccountRegistrationMapper implements Mapper<RegistrationDto, Account> {

  private final PasswordEncoder passwordEncoder;

  @Override
  public Account map(RegistrationDto dto) {
    return Account.builder()
        .email(dto.email())
        .username(dto.username())
        .password(passwordEncoder.encode(dto.password()))
        .role(dto.role() != null ? dto.role() : Role.USER)
        .enabled(Boolean.TRUE)
        .build();
  }
}
