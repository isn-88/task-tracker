package su.itpro.tasktracker.integration.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestUserSecurity;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;
import su.itpro.tasktracker.repository.AccountRepository;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class TaskControllerIT extends IntegrationTestUserSecurity {

  private static final String EMAIL = "user@email.com";
  private static final String USERNAME = "user";
  private static final String PASSWORD = "password";

  private final AccountRepository accountRepository;
  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  @BeforeEach
  void prepare() {
    Account account = Account.builder()
        .email(EMAIL)
        .username(USERNAME)
        .password("{noop}" + PASSWORD)
        .role(Role.USER)
        .build();
    accountRepository.save(account);
    Project project = Project.builder()
        .name("TestProject")
        .build();
    entityManager.persist(project);
    Task parentTask = Task.builder()
        .title("Task first")
        .project(project)
        .owner(account)
        .type(TaskType.FEATURE)
        .status(TaskStatus.ASSIGNED)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(parentTask);
    Task childTask = Task.builder()
        .parent(parentTask)
        .title("Task second")
        .project(project)
        .owner(account)
        .type(TaskType.FEATURE)
        .status(TaskStatus.ASSIGNED)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(childTask);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void findAll() throws Exception {
    mockMvc.perform(get("/api/v1/tasks").param("findPattern", "task"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.success").value(true),
            jsonPath("$.length()").value(2)
        );
  }

}