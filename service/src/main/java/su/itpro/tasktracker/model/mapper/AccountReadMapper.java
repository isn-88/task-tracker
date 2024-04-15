package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.AccountReadDto;
import su.itpro.tasktracker.model.entity.Account;

@Component
public class AccountReadMapper implements Mapper<Account, AccountReadDto> {

  @Override
  public AccountReadDto map(Account account) {
    return AccountReadDto.builder()
        .id(account.getId())
        .email(account.getEmail())
        .username(account.getUsername())
        // TODO check profile is not null
        .lastname(account.getProfile().getLastname())
        .firstname(account.getProfile().getFirstname())
        .surname(account.getProfile().getSurname())
        .gender(account.getProfile().getGender())
        .aboutMe(account.getProfile().getAboutMe())
        .build();
  }

}
