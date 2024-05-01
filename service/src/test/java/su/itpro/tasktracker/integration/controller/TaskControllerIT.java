package su.itpro.tasktracker.integration.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static su.itpro.tasktracker.model.dto.TaskFilter.Fields.ownerId;
import static su.itpro.tasktracker.model.dto.TaskUpdateDto.Fields.priority;
import static su.itpro.tasktracker.model.dto.TaskUpdateDto.Fields.projectId;
import static su.itpro.tasktracker.model.dto.TaskUpdateDto.Fields.status;
import static su.itpro.tasktracker.model.dto.TaskUpdateDto.Fields.title;
import static su.itpro.tasktracker.model.dto.TaskUpdateDto.Fields.type;
import static su.itpro.tasktracker.model.dto.TaskFilter.Fields.types;

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

  private Account account;
  private Task savedTask;
  private Project savedProject;

  @BeforeEach
  void prepare() {
    account = Account.builder()
        .email(EMAIL)
        .username(USERNAME)
        .password("{noop}" + PASSWORD)
        .role(Role.USER)
        .build();
    accountRepository.save(account);
    savedProject = Project.builder()
        .name("Test project")
        .build();
    entityManager.persist(savedProject);
    savedTask = Task.builder()
        .title("Test task")
        .project(savedProject)
        .owner(account)
        .type(TaskType.FEATURE)
        .status(TaskStatus.ASSIGNED)
        .priority(TaskPriority.HIGH)
        .build();
    entityManager.persist(savedTask);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void findAll() throws Exception {
    mockMvc.perform(get("/tasks")
                        .queryParam(types, "FEATURE"))
        .andExpectAll(
            status().is2xxSuccessful(),
            view().name("task/tasks"),
            model().attributeExists("tasks")
        );
  }

  @Test
  void findById() throws Exception {
    mockMvc.perform(get("/tasks/{id}", savedTask.getId()))
        .andExpectAll(
            status().is2xxSuccessful(),
            view().name("task/task"),
            model().attributeExists("task")
        );
  }

  @Test
  void create() throws Exception {
    mockMvc.perform(post("/tasks")
                        .param(projectId, savedProject.getId().toString())
                        .param(ownerId, account.getId().toString())
                        .param(type, "FEATURE")
                        .param(title, "Title create")
                        .param(status, "NEW")
                        .param(priority, "NORMAL")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrlPattern("/tasks/{\\d+}")
        );
  }

  @Test
  void update() throws Exception {
    mockMvc.perform(post("/tasks/{id}/update", savedTask.getId())
                        .param(projectId, savedProject.getId().toString())
                        .param(type, "FEATURE")
                        .param(title, "Title update")
                        .param(status, "ASSIGNED")
                        .param(priority, "HIGH")
                        .with(csrf()))
        .andExpectAll(
            status().is3xxRedirection(),
            redirectedUrlPattern("/tasks/{\\d+}")
        );
  }

}