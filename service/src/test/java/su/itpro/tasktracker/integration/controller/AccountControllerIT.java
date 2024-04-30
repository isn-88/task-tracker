package su.itpro.tasktracker.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static su.itpro.tasktracker.model.dto.AccountUpdateDto.Fields.email;
import static su.itpro.tasktracker.model.dto.AccountUpdateDto.Fields.username;
import static su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields.currentPassword;
import static su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields.newPassword;
import static su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields.repeatPassword;
import static su.itpro.tasktracker.model.dto.ProfileUpdateDto.Fields.firstname;
import static su.itpro.tasktracker.model.dto.ProfileUpdateDto.Fields.lastname;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestUserSecurity;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AccountControllerIT extends IntegrationTestUserSecurity {

  private static final String EMAIL = "user@email.com";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static final String LASTNAME = "Lastname";
  private static final String FIRSTNAME = "Firstname";

  private final AccountRepository accountRepository;
  private final EntityManager entityManager;
  private final PasswordEncoder passwordEncoder;
  private final MockMvc mockMvc;

  @BeforeEach
  void prepare() {
    Account account = Account.builder()
        .email(EMAIL)
        .username(USERNAME)
        .password("{noop}" + PASSWORD)
        .role(Role.USER)
        .build();
    Profile profile = Profile.builder()
        .lastname(LASTNAME)
        .firstname(FIRSTNAME)
        .build();
    accountRepository.save(account);
    profile.setAccount(account);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void accountEditPage() throws Exception {
    mockMvc.perform(get("/account/edit"))
        .andExpectAll(
            status().is2xxSuccessful(),
            view().name("account/edit"),
            model().attributeExists("account")
        );
  }

  @Test
  void updateAccount_success() throws Exception {
    String updatedEmail = "updated-user@email.com";
    String updatedUsername = "updated-user";
    mockMvc.perform(post("/account")
                        .param(email, updatedEmail)
                        .param(username, updatedUsername)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/logout")
        );

    Optional<Account> actualResult = accountRepository.findByUsername(updatedUsername);
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().getEmail()).isEqualTo(updatedEmail);
  }

  @Test
  void updateAccount_failedUsernameTooShort() throws Exception {
    String updatedEmail = "updated-user@email.com";
    String updatedUsername = "us";
    mockMvc.perform(post("/account")
                        .param(email, updatedEmail)
                        .param(username, updatedUsername)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account/edit"),
            flash().attributeExists("binding")
        );

    Optional<Account> updatedResult = accountRepository.findByUsername(updatedUsername);
    assertThat(updatedResult).isNotPresent();

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().getEmail()).isEqualTo(EMAIL);
  }

  @Test
  void updateAccount_failedPatternUsername() throws Exception {
    String updatedEmail = "updated-user@email.com";
    String updatedUsername = "1user@name";
    mockMvc.perform(post("/account")
                        .param(email, updatedEmail)
                        .param(username, updatedUsername)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account/edit"),
            flash().attributeExists("binding")
        );

    Optional<Account> updatedResult = accountRepository.findByUsername(updatedUsername);
    assertThat(updatedResult).isNotPresent();

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().getEmail()).isEqualTo(EMAIL);
  }

  @Test
  void updateProfile_success() throws Exception {
    String updatedFirstname = "Firstname-updated";
    String updatedLastname = "Lastname-updated";
    mockMvc.perform(post("/account/profile")
                        .param(lastname, updatedLastname)
                        .param(firstname, updatedFirstname)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account/edit")
        );

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().getProfile().getFirstname()).isEqualTo(updatedFirstname);
    assertThat(actualResult.get().getProfile().getLastname()).isEqualTo(updatedLastname);
  }

  @Test
  void updateProfile_incorrectParams() throws Exception {
    mockMvc.perform(post("/account/profile")
                        .param(lastname, "")
                        .param(firstname, "")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account/edit"),
            flash().attributeExists("binding")
        );

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().getProfile().getFirstname()).isEqualTo(FIRSTNAME);
    assertThat(actualResult.get().getProfile().getLastname()).isEqualTo(LASTNAME);
  }

  @Test
  void updatePassword_success() throws Exception {
    String inputNewPassword = "updated";
    mockMvc.perform(post("/account/password")
                        .param(currentPassword, PASSWORD)
                        .param(newPassword, inputNewPassword)
                        .param(repeatPassword, inputNewPassword)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/login")
        );

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertTrue(passwordEncoder.matches(inputNewPassword, actualResult.get().getPassword()));
  }

  @Test
  void updatePassword_incorrectCurrentPassword() throws Exception {
    String inputCurrentPassword = "incorrect";
    String inputNewPassword = "updated";
    mockMvc.perform(post("/account/password")
                        .param(currentPassword, inputCurrentPassword)
                        .param(newPassword, inputNewPassword)
                        .param(repeatPassword, inputNewPassword)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account/edit"),
            flash().attributeExists("binding")
        );

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertTrue(passwordEncoder.matches(PASSWORD, actualResult.get().getPassword()));
  }

  @Test
  void updatePassword_repeatPasswordMismatch() throws Exception {
    String inputNewPassword = "updated";
    String inputRepeatPassword = "mismatch";
    mockMvc.perform(post("/account/password")
                        .param(currentPassword, PASSWORD)
                        .param(newPassword, inputNewPassword)
                        .param(repeatPassword, inputRepeatPassword)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account/edit"),
            flash().attributeExists("binding")
        );

    Optional<Account> actualResult = accountRepository.findByUsername(USERNAME);
    assertThat(actualResult).isPresent();
    assertTrue(passwordEncoder.matches(PASSWORD, actualResult.get().getPassword()));
  }
}
