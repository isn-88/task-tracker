package su.itpro.tasktracker.model.mapper;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.entity.Account;

@Component
public class UserDetailsMapper implements Mapper<Account, UserDetails> {

  @Override
  public UserDetails map(Account account) {
    return User.builder()
        .username(account.getUsername())
        .password(account.getPassword())
        .authorities(account.getRole())
        .disabled(!account.getEnabled())
        .build();
  }
}
