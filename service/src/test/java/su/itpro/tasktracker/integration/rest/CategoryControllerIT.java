package su.itpro.tasktracker.integration.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import su.itpro.tasktracker.integration.IntegrationTestBase;
import su.itpro.tasktracker.model.entity.Category;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class CategoryControllerIT extends IntegrationTestBase {

  private final EntityManager entityManager;
  private final MockMvc mockMvc;

  private Category categoryOne;
  private Category categoryTwo;

  @BeforeEach
  void prepare() {
    categoryOne = Category.builder()
        .name("test")
        .build();
    categoryTwo = Category.builder()
        .name("general")
        .build();
    entityManager.persist(categoryOne);
    entityManager.persist(categoryTwo);
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void findAll() throws Exception {
    mockMvc.perform(get("/api/v1/categories"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.success").value(true),
            jsonPath("$.length()").value(2)
        );
  }

  @Test
  void findById_success() throws Exception {
    mockMvc.perform(get("/api/v1/categories/{id}", categoryOne.getId()))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.success").value(true),
            jsonPath("$.data.id").value(categoryOne.getId().intValue()),
            jsonPath("$.data.name").value(categoryOne.getName())
        );
  }

  @Test
  void findById_failure() throws Exception {
    mockMvc.perform(get("/api/v1/categories/{id}", Short.MAX_VALUE))
        .andExpectAll(
            status().isNotFound(),
            jsonPath("$.success").value(false),
            jsonPath("$.message").isString()
        );
  }

  @Test
  void create_success() throws Exception {
    mockMvc.perform(post("/api/v1/categories")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":  \"testCreate\"}"))
        .andExpectAll(
            status().isCreated(),
            jsonPath("$.success").value(true),
            jsonPath("$.data.id").isNumber(),
            jsonPath("$.data.name").value("testCreate")
        );
  }

  @Test
  void create_failure() throws Exception {
    mockMvc.perform(post("/api/v1/categories")
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":  \"\"}"))
        .andExpectAll(
            status().is4xxClientError(),
            jsonPath("$.success").value(false),
            jsonPath("$.message").isString()
        );
  }

  @Test
  void update_success() throws Exception {
    mockMvc.perform(put("/api/v1/categories/{id}", categoryTwo.getId())
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":  \"testUpdate\"}"))
        .andExpectAll(
            status().isOk(),
            jsonPath("$.success").value(true),
            jsonPath("$.data.id").isNumber(),
            jsonPath("$.data.name").value("testUpdate")
        );
  }

  @Test
  void update_failure() throws Exception {
    mockMvc.perform(put("/api/v1/categories/{id}", Short.MAX_VALUE)
                        .contentType(APPLICATION_JSON)
                        .content("{\"name\":  \"testUpdate\"}"))
        .andExpectAll(
            status().isNotFound(),
            jsonPath("$.success").value(false),
            jsonPath("$.message").isString()
        );
  }

}