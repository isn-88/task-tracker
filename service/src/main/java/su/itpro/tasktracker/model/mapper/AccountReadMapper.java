package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.AccountReadDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.util.ProfileUtil;

@Component
public class AccountReadMapper implements Mapper<Account, AccountReadDto> {

  @Override
  public AccountReadDto map(Account account) {
    return AccountReadDto.builder()
        .id(account.getId())
        .email(account.getEmail())
        .username(account.getUsername())
        .role(account.getRole())
        .isEnabled(account.getEnabled())
        .createdAt(account.getCreatedAt())
        .lastname(account.getProfile().getLastname())
        .firstname(account.getProfile().getFirstname())
        .surname(account.getProfile().getSurname())
        .fullName(ProfileUtil.getFullName(account.getProfile()))
        .gender(account.getProfile().getGender())
        .aboutMe(account.getProfile().getAboutMe())
        .build();
  }

}
