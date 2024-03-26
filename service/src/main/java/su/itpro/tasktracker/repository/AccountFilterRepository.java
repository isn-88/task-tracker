package su.itpro.tasktracker.repository;

import java.util.Optional;
import su.itpro.tasktracker.model.dto.AccountLoginDto;
import su.itpro.tasktracker.model.entity.Account;

public interface AccountFilterRepository {

  Optional<Account> findByFilter(AccountLoginDto loginDto);

}
