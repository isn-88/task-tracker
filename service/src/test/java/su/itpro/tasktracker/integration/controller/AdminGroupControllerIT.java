package su.itpro.tasktracker.integration.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestAdminSecurity;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AdminGroupControllerIT extends IntegrationTestAdminSecurity {

  private static final String EMAIL = "admin@email.com";
  private static final String USERNAME = "admin";
  private static final String PASSWORD = "password";

  private final AccountRepository accountRepository;
  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  @BeforeEach
  void prepare() {
    Account userAccount = Account.builder()
        .email("user@email.com")
        .username("user")
        .password("{noop}" + PASSWORD)
        .role(Role.USER)
        .build();
    accountRepository.save(userAccount);
    Account adminAccount = Account.builder()
        .email(EMAIL)
        .username(USERNAME)
        .password("{noop}" + PASSWORD)
        .role(Role.ADMIN)
        .build();
    accountRepository.save(adminAccount);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void getGroupPage() throws Exception {
    mockMvc.perform(get("/admin/group"))
        .andExpectAll(
            status().isOk(),
            view().name("admin/group"),
            model().attributeExists("groups"),
            model().attributeExists("tab")
        );
  }

  @Test
  @WithMockUser(username = "user", password = "{noop}password", authorities = {"USER"})
  void getGroup_forbidden() throws Exception {
    mockMvc.perform(get("/admin/group"))
        .andExpectAll(
            status().isForbidden()
        );
  }

  @Test
  void createGroup() throws Exception {
    mockMvc.perform(post("/admin/group")
                        .param("name", "new-group")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            view().name("redirect:/admin/group"),
            model().hasNoErrors()
        );
  }

}
