package su.itpro.tasktracker.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import su.itpro.tasktracker.integration.annotation.IT;

@IT
public abstract class IntegrationTestBase {

  private static final String IMAGE_NAME = "postgres:16";

  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(IMAGE_NAME);

  @BeforeAll
  static void startContainer() {
    postgres.start();
  }

  @DynamicPropertySource
  static void postgresProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
  }

}
