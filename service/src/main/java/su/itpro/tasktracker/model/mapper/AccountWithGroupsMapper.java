package su.itpro.tasktracker.model.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.AccountWithGroupsDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.GroupAccount;

@Component
@RequiredArgsConstructor
public class AccountWithGroupsMapper implements Mapper<Account, AccountWithGroupsDto> {

  private final AccountReadMapper accountReadMapper;
  private final GroupReadMapper groupReadMapper;

  @Override
  public AccountWithGroupsDto map(Account account) {
    return AccountWithGroupsDto.builder()
        .account(accountReadMapper.map(account))
        .groups(account.getGroupAccounts().stream()
                    .map(GroupAccount::getGroup)
                    .map(groupReadMapper::map)
                    .toList())
        .build();
  }
}
