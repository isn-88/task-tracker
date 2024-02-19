package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Account;
import su.itpro.model.entity.Profile;
import su.itpro.model.enums.Gender;
import su.itpro.model.enums.Role;
import su.itpro.util.HibernateTestUtil;

public class AccountIT {

  private static SessionFactory sessionFactory;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.getSessionFactory();
  }

  @BeforeEach
  void prepare() {
    session = sessionFactory.openSession();
    session.beginTransaction();
  }

  @Test
  void testCreateAccount() {
    Account account = Account.builder()
        .email("test-create@email.com")
        .login("test-create")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    session.persist(account);
    profile.setAccount(account);
    session.flush();
    session.evict(account);

    Account actualResult = session.get(Account.class, account.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(account);
    assertThat(actualResult.getProfile()).isNotNull();
    assertThat(actualResult.getProfile().getId()).isEqualTo(actualResult.getId());
  }

  @Test
  void testReadExistsAccount() {
    Account account1 = Account.builder()
        .email("test-1@email.com")
        .login("test-1")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile1 = Profile.builder()
        .lastname("Lastname_1")
        .firstname("Firstname_1")
        .build();
    Account account2 = Account.builder()
        .email("test-2@email.com")
        .login("test-2")
        .password("password")
        .role(Role.ADMIN)
        .build();
    Profile profile2 = Profile.builder()
        .lastname("Lastname_2")
        .firstname("Firstname_2")
        .build();
    session.persist(account1);
    session.persist(account2);
    profile1.setAccount(account1);
    profile2.setAccount(account2);
    session.flush();
    session.evict(account1);
    session.evict(account2);

    Account actualResult = session.get(Account.class, account1.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(account1);
    assertThat(actualResult.getProfile()).isNotNull();
    assertThat(actualResult.getProfile().getId()).isEqualTo(actualResult.getId());
  }

  @Test
  void testReadNotExistsAccount() {
    Account account = Account.builder()
        .email("test-not-exist@email.com")
        .login("test-not-exist")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    session.persist(account);
    profile.setAccount(account);
    session.flush();
    session.evict(account);

    Account actualResult = session.get(Account.class, UUID.randomUUID());

    assertThat(actualResult).isNull();
  }

  @Test
  void testUpdateAccount() {
    Account account = Account.builder()
        .email("test-update@email.com")
        .login("test-update")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    session.persist(account);
    profile.setAccount(account);
    session.flush();
    account.setEmail("updated@email.com");
    profile.setGender(Gender.MALE);
    session.flush();
    session.evict(account);

    Account actualResult = session.get(Account.class, account.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult.getId()).isEqualTo(account.getId());
    assertThat(actualResult.getEmail()).isEqualTo(account.getEmail());
    assertThat(actualResult.getProfile().getId()).isEqualTo(actualResult.getId());
    assertThat(actualResult.getProfile().getGender()).isEqualTo(Gender.MALE);
  }

  @Test
  void testDeleteAccount() {
    Account account = Account.builder()
        .email("test-delete@email.com")
        .login("test-delete")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    session.persist(account);
    profile.setAccount(account);
    session.flush();

    session.remove(account);
    session.flush();

    Account actualAccountResult = session.get(Account.class, account.getId());
    Profile actualProfileResult = session.get(Profile.class, profile.getId());
    assertThat(actualAccountResult).isNull();
    assertThat(actualProfileResult).isNull();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
    session.close();
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

}
