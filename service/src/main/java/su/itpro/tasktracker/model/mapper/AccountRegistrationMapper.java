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
        .email(dto.getEmail())
        .username(dto.getUsername())
        .password(passwordEncoder.encode(dto.getPassword()))
        .role(dto.getRole() != null ? dto.getRole() : Role.USER)
        .enabled(Boolean.TRUE)
        .build();
  }
}
