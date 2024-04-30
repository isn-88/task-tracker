package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.AccountAboutDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.util.ProfileUtil;

@Component
public class AccountAboutMapper implements Mapper<Account, AccountAboutDto> {

  @Override
  public AccountAboutDto map(Account account) {
    return AccountAboutDto.builder()
        .id(account.getId())
        .username(account.getUsername())
        .email(account.getEmail())
        .fullName(ProfileUtil.getFullName(account.getProfile()))
        .createdAt(account.getCreatedAt())
        .lastLoginAt(account.getLastLoginAt())
        .build();
  }

}
