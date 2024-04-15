package su.itpro.tasktracker.integration.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestSecurity;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AccountControllerIT extends IntegrationTestSecurity {

  private final AccountRepository accountRepository;
  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  @BeforeEach
  void prepare() {
    Account account = Account.builder()
        .email("test-user@email.com")
        .username("user")
        .password("{noop}pass")
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
  }

  @Test
  void updateAccount_success() throws Exception {
    mockMvc.perform(post("/account")
                        .param("email", "test-update@email.com")
                        .param("username", "test-update")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/logout")
        );
  }

  @Test
  void updateAccount_failedUsername() throws Exception {
    mockMvc.perform(post("/account")
                        .param("email", "test-update@email.com")
                        .param("username", "ab")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account"),
            flash().attributeExists("binding")
        );
  }

  @Test
  void updateProfile_success() throws Exception {
    mockMvc.perform(post("/account/profile")
                        .param("lastname", "Lastname")
                        .param("firstname", "Firstname")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account")
        );
  }

  @Test
  void updateProfile_incorrectParams() throws Exception {
    mockMvc.perform(post("/account/profile")
                        .param("lastname", "")
                        .param("firstname", "")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl("/account"),
            flash().attributeExists("binding")
        );
  }

}
