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
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class AdminAccountControllerIT extends IntegrationTestAdminSecurity {

  private static final String EMAIL = "user@email.com";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";
  private static final String LASTNAME = "Lastname";
  private static final String FIRSTNAME = "Firstname";

  private final AccountRepository accountRepository;
  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  private Account userAccount;
  private Account adminAccount;

  @BeforeEach
  void prepare() {
    userAccount = Account.builder()
        .email(EMAIL)
        .username(USERNAME)
        .password("{noop}" + PASSWORD)
        .role(Role.USER)
        .build();
    Profile userProfile = Profile.builder()
        .lastname(LASTNAME)
        .firstname(FIRSTNAME)
        .build();
    accountRepository.save(userAccount);
    userProfile.setAccount(userAccount);

    adminAccount = Account.builder()
        .email("admin@email.com")
        .username("admin")
        .password("{noop}" + PASSWORD)
        .role(Role.ADMIN)
        .build();
    Profile adminProfile = Profile.builder()
        .lastname(LASTNAME)
        .firstname(FIRSTNAME)
        .build();
    accountRepository.save(adminAccount);
    adminProfile.setAccount(adminAccount);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void accountEditPage() throws Exception {
    mockMvc.perform(get("/admin/account/find"))
        .andExpectAll(
            status().isOk(),
            view().name("admin/account-find"),
            model().attributeExists("filter"),
            model().attributeExists("roles"),
            model().attributeExists("accounts"),
            model().attributeExists("currentPage"),
            model().attributeExists("totalPages"),
            model().attributeExists("totalItems"),
            model().attributeExists("pageSize"),
            model().attributeExists("navPages"),
            model().attributeExists("register"),
            model().attributeExists("tab")
        );
  }

  @Test
  @WithMockUser(username = "user", password = "{noop}password", authorities = {"USER"})
  void accountEditPage_forbidden() throws Exception {
    mockMvc.perform(get("/admin/account/find"))
        .andExpectAll(
            status().isForbidden()
        );
  }

  @Test
  void getCreatePage() throws Exception {
    mockMvc.perform(get("/admin/account/create"))
        .andExpectAll(
            status().isOk(),
            view().name("admin/account-create"),
            model().attributeExists("register"),
            model().attributeExists("roles"),
            model().attributeExists("tab")
        );
  }

  @Test
  @WithMockUser(username = "user", password = "{noop}password", authorities = {"USER"})
  void getCreatePage_forbidden() throws Exception {
    mockMvc.perform(get("/admin/account/create"))
        .andExpectAll(
            status().isForbidden()
        );
  }

  @Test
  void createNewAccount() throws Exception {
    mockMvc.perform(post("/admin/account/create")
                        .param("email", "create@email.com")
                        .param("username", "create")
                        .param("password", PASSWORD)
                        .param("lastname", LASTNAME)
                        .param("firstname", FIRSTNAME)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            view().name("redirect:/admin/account/find"),
            model().hasNoErrors()
        );
  }

  @Test
  void createNewAccount_alreadyExists() throws Exception {
    mockMvc.perform(post("/admin/account/create")
                        .param("email", EMAIL)
                        .param("username", "create")
                        .param("password", PASSWORD)
                        .param("lastname", LASTNAME)
                        .param("firstname", FIRSTNAME)
                        .with(csrf()))
        .andExpectAll(
            status().isOk(),
            view().name("admin/account-create"),
            model().attributeExists("tab"),
            model().attributeHasErrors("register"),
            model().errorCount(1)
        );
  }

  @Test
  @WithMockUser(username = "user", password = "{noop}password", authorities = {"USER"})
  void blockAccount_forbidden() throws Exception {
    mockMvc.perform(get("/admin/account/block"))
        .andExpectAll(
            status().isForbidden()
        );
  }

  @Test
  void blockAccount() throws Exception {
    mockMvc.perform(post("/admin/account/block")
                        .param("id", userAccount.getId().toString())
                        .param("isEnabled", "false")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            view().name("redirect:/admin/account/find"),
            model().hasNoErrors()
        );
  }

  @Test
  void blockAccount_selfBlock() throws Exception {
    mockMvc.perform(post("/admin/account/block")
                        .param("id", adminAccount.getId().toString())
                        .param("isEnabled", "false")
                        .with(csrf()))
        .andExpectAll(
            status().isOk(),
            view().name("error/errorPage"),
            model().hasNoErrors()
        );
  }

}
