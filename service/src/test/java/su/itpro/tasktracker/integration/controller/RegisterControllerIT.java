package su.itpro.tasktracker.integration.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.email;
import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.firstname;
import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.lastname;
import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.password;
import static su.itpro.tasktracker.model.dto.RegisterDto.Fields.username;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestBase;
import su.itpro.tasktracker.model.dto.RegisterDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class RegisterControllerIT extends IntegrationTestBase {

  private static final String SUCCESS_URL = "/login?register=true";
  private static final String CONTROLLER_URL = "/registration";
  private static final String ATTRIBUTE_NAME = "register";
  private static final String EMAIL = "use@email.com";
  private static final String USERNAME = "use_name";
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
  void register_success() throws Exception {
    RegisterDto dto = getCorrectDto();
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl(SUCCESS_URL),
            model().hasNoErrors()
        );

    Optional<Account> actualResult = accountRepository.findByUsername(dto.getUsername());
    assertThat(actualResult).isPresent();
    assertThat(actualResult.get().getEmail()).isEqualTo(dto.getEmail());
    assertTrue(passwordEncoder.matches(dto.getPassword(), actualResult.get().getPassword()));
    assertThat(actualResult.get().getProfile()).isNotNull();
    assertThat(actualResult.get().getProfile().getLastname()).isEqualTo(dto.getLastname());
    assertThat(actualResult.get().getProfile().getFirstname()).isEqualTo(dto.getFirstname());
  }

  @Test
  void registerFailed_emailInUse() throws Exception {
    RegisterDto dto = getCorrectDto();
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, EMAIL)
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, email),
            model().attributeErrorCount(ATTRIBUTE_NAME, 1)
        );
  }

  @Test
  void registerFailed_usernameInUse() throws Exception {
    RegisterDto dto = getCorrectDto();
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, USERNAME)
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, username),
            model().attributeErrorCount(ATTRIBUTE_NAME, 1)
        );
  }

  @Test
  void validationEmail_success() throws Exception {
    RegisterDto dto = getCorrectDto();
    String validEmail = "new.user@email.com";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, validEmail)
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl(SUCCESS_URL),
            model().hasNoErrors()
        );
  }

  @Test
  void validationEmail_invalid() throws Exception {
    RegisterDto dto = getCorrectDto();
    String invalidEmail = "user.com";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, invalidEmail)
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, email),
            model().attributeErrorCount(ATTRIBUTE_NAME, 1)
        );
  }

  @Test
  void validationUsername_success() throws Exception {
    RegisterDto dto = getCorrectDto();
    String validUsername = "test.user_name-1";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, validUsername)
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl(SUCCESS_URL),
            model().hasNoErrors()
        );
  }

  @Test
  void validationUsername_invalid() throws Exception {
    RegisterDto dto = getCorrectDto();
    String invalidUsername = "1@";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, invalidUsername)
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, username),
            model().attributeErrorCount(ATTRIBUTE_NAME, 2)
        );
  }

  @Test
  void validationPassword_success() throws Exception {
    RegisterDto dto = getCorrectDto();
    String validPassword = "pa$$w0rD";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, validPassword)
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl(SUCCESS_URL),
            model().hasNoErrors()
        );
  }

  @Test
  void validationPassword_invalid() throws Exception {
    RegisterDto dto = getCorrectDto();
    String invalidPassword = "p w";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, invalidPassword)
                        .param(lastname, dto.getLastname())
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, password),
            model().attributeErrorCount(ATTRIBUTE_NAME, 1)
        );
  }

  @Test
  void validationLastname_success() throws Exception {
    RegisterDto dto = getCorrectDto();
    String validLastname = "Doe";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, validLastname)
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl(SUCCESS_URL),
            model().hasNoErrors()
        );
  }

  @Test
  void validationLastname_invalid() throws Exception {
    RegisterDto dto = getCorrectDto();
    String invalidLastname = "D";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, invalidLastname)
                        .param(firstname, dto.getFirstname())
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, lastname),
            model().attributeErrorCount(ATTRIBUTE_NAME, 1)
        );
  }

  @Test
  void validationFirstname_success() throws Exception {
    RegisterDto dto = getCorrectDto();
    String validFirstname = "John";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, validFirstname)
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrl(SUCCESS_URL),
            model().hasNoErrors()
        );
  }

  @Test
  void validationFirstname_invalid() throws Exception {
    RegisterDto dto = getCorrectDto();
    String invalidFirstname = "J";
    mockMvc.perform(post(CONTROLLER_URL)
                        .param(email, dto.getEmail())
                        .param(username, dto.getUsername())
                        .param(password, dto.getPassword())
                        .param(lastname, dto.getLastname())
                        .param(firstname, invalidFirstname)
                        .with(csrf()))
        .andExpectAll(
            status().is2xxSuccessful(),
            model().attributeHasFieldErrors(ATTRIBUTE_NAME, firstname),
            model().attributeErrorCount(ATTRIBUTE_NAME, 1)
        );
  }

  private RegisterDto getCorrectDto() {
    return RegisterDto.builder()
        .email("test@email.com")
        .username("username")
        .password("pass")
        .lastname("Lastname")
        .firstname("Firstname")
        .build();
  }
}
