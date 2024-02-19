package su.itpro;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Profile;
import su.itpro.model.enums.Role;
import su.itpro.util.datasource.HibernateUtil;

@Slf4j
public class HibernateRunner {

  public static void main(String[] args) {
    try (SessionFactory sessionFactory = HibernateUtil.getSessionFactory()) {
      Account savedAccount = sessionFactory.fromTransaction(
          session -> {
            int random = new Random().nextInt(1_000);
            Account account = Account.builder()
                .email("test-" + random + "@email.com")
                .login("login-" + random)
                .password("password")
                .role(Role.USER)
                .build();
            session.persist(account);
            Profile profile = Profile.builder()
                .lastname("Тестовый")
                .firstname("Пользователь")
                .build();
            profile.setAccount(account);
            return account;
          }
      );

      Account readAccount = sessionFactory.fromTransaction(
          session -> session.get(Account.class, savedAccount.getId())
      );
      log.info("Read account: {}", readAccount);
      log.info("Read profile: {}", readAccount.getProfile());
    }
  }
}
