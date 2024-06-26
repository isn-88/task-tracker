package su.itpro.tasktracker.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.integration.IntegrationTestBase;
import su.itpro.tasktracker.model.dto.AccountLoginDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.enums.Gender;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.ProfileRepository;

@RequiredArgsConstructor
public class AccountIT extends IntegrationTestBase {

  private final AccountRepository accountRepository;
  private final ProfileRepository profileRepository;
  private final EntityManager entityManager;

  @Test
  void createAccount() {
    Account account = Account.builder()
        .email("test-create@email.com")
        .username("test-create")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    accountRepository.save(account);
    profile.setAccount(account);
    entityManager.flush();
    entityManager.clear();

    Optional<Account> actualResult = accountRepository.findById(account.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(account);
    assertThat(actualResult.get().getProfile()).isEqualTo(account.getProfile());
  }

  @Test
  void readExistsAccount() {
    Account account1 = Account.builder()
        .email("test-1@email.com")
        .username("test-1")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile1 = Profile.builder()
        .lastname("Lastname_1")
        .firstname("Firstname_1")
        .build();
    Account account2 = Account.builder()
        .email("test-2@email.com")
        .username("test-2")
        .password("password")
        .role(Role.ADMIN)
        .build();
    Profile profile2 = Profile.builder()
        .lastname("Lastname_2")
        .firstname("Firstname_2")
        .build();
    accountRepository.save(account1);
    accountRepository.save(account2);
    profile1.setAccount(account1);
    profile2.setAccount(account2);
    entityManager.flush();
    entityManager.clear();

    Optional<Account> actualResult = accountRepository.findById(account1.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(account1);
    assertThat(actualResult.get().getProfile()).isEqualTo(profile1);
  }

  @Test
  void readNotExistsAccount() {
    Account account = Account.builder()
        .email("test-not-exist@email.com")
        .username("test-not-exist")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    accountRepository.save(account);
    profile.setAccount(account);
    entityManager.flush();
    entityManager.clear();

    Optional<Account> actualResult = accountRepository.findById(Long.MAX_VALUE);

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateAccount() {
    Account account = Account.builder()
        .email("old@email.com")
        .username("test-update")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    accountRepository.save(account);
    profile.setAccount(account);
    entityManager.flush();
    entityManager.clear();
    account.setEmail("new@email.com");
    profile.setGender(Gender.MALE);
    accountRepository.save(account);
    entityManager.flush();
    entityManager.clear();

    Optional<Account> actualResult = accountRepository.findById(account.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(account);
    assertThat(actualResult.get().getProfile()).isEqualTo(profile);
  }

  @Test
  void deleteAccount() {
    Account account = Account.builder()
        .email("test-delete@email.com")
        .username("test-delete")
        .password("password")
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
    accountRepository.save(account);
    profile.setAccount(account);
    entityManager.flush();

    accountRepository.delete(account);

    Optional<Account> actualAccountResult = accountRepository.findById(account.getId());
    Optional<Profile> actualProfileResult = profileRepository.findById(profile.getId());
    assertThat(actualAccountResult).isEmpty();
    assertThat(actualProfileResult).isEmpty();
  }

  @Test
  void findByUsername_notFound() {
    Account account1 = Account.builder()
        .email("test-1@email.com")
        .username("test-1")
        .password("password")
        .role(Role.USER)
        .build();
    Account account2 = Account.builder()
        .email("test-2@email.com")
        .username("test-2")
        .password("password")
        .role(Role.ADMIN)
        .build();
    accountRepository.save(account1);
    accountRepository.save(account2);
    entityManager.flush();
    entityManager.clear();
    AccountLoginDto loginDto = AccountLoginDto.builder()
        .username("test")
        .build();

    Optional<Account> actualResult = accountRepository.findByFilter(loginDto);

    assertThat(actualResult).isEmpty();
  }

  @Test
  void findByUsername_success() {
    String username = "test-1";
    Account account1 = Account.builder()
        .email("test-1@email.com")
        .username(username)
        .password("password")
        .role(Role.USER)
        .build();
    Account account2 = Account.builder()
        .email("test-2@email.com")
        .username("test-2")
        .password("password")
        .role(Role.ADMIN)
        .build();
    accountRepository.save(account1);
    accountRepository.save(account2);
    entityManager.flush();
    entityManager.clear();
    AccountLoginDto loginDto = AccountLoginDto.builder()
        .username(username)
        .build();

    Optional<Account> actualResult = accountRepository.findByFilter(loginDto);

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(account1);
  }

  @Test
  void findByEmail_success() {
    String email = "test-2@email.com";
    Account account1 = Account.builder()
        .email("test-1@email.com")
        .username("test-1")
        .password("password")
        .role(Role.USER)
        .build();
    Account account2 = Account.builder()
        .email(email)
        .username("test-2")
        .password("password")
        .role(Role.ADMIN)
        .build();
    accountRepository.save(account1);
    accountRepository.save(account2);
    entityManager.flush();
    entityManager.clear();
    AccountLoginDto loginDto = AccountLoginDto.builder()
        .email(email)
        .build();

    Optional<Account> actualResult = accountRepository.findByFilter(loginDto);

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(account2);
  }

  @Test
  void findByUsernameAndEmail_filed() {
    String username = "test-1";
    String email = "test-2@email.com";
    Account account1 = Account.builder()
        .email("test-1@email.com")
        .username(username)
        .password("password")
        .role(Role.USER)
        .build();
    Account account2 = Account.builder()
        .email(email)
        .username("test-2")
        .password("password")
        .role(Role.ADMIN)
        .build();
    accountRepository.save(account1);
    accountRepository.save(account2);
    entityManager.flush();
    entityManager.clear();
    AccountLoginDto loginDto = AccountLoginDto.builder()
        .username(username)
        .email(email)
        .build();

    assertThatThrownBy(() -> accountRepository.findByFilter(loginDto))
        .hasCauseInstanceOf(NonUniqueResultException.class);
  }

}
