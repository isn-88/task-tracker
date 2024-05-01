package su.itpro.tasktracker.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestUserSecurity;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AccountAboutControllerIT extends IntegrationTestUserSecurity {

  private static final String EMAIL = "user@email.com";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";

  private final AccountRepository accountRepository;
  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  private Account savedAccount;

  @BeforeEach
  void prepare() {
    savedAccount = Account.builder()
        .email(EMAIL)
        .username(USERNAME)
        .password("{noop}" + PASSWORD)
        .role(Role.USER)
        .build();
    accountRepository.save(savedAccount);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void accountAboutPage() throws Exception {
    mockMvc.perform(get("/accounts/{id}", savedAccount.getId()))
        .andExpectAll(
            status().is2xxSuccessful(),
            view().name("account/about"),
            model().attributeExists("account")
        );
  }

}
