package su.itpro.tasktracker.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import su.itpro.tasktracker.util.datasource.HibernateUtil;

@UtilityClass
public class HibernateTestUtil {

  private final String IMAGE_NAME = "postgres:16";

  private final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(IMAGE_NAME);

  static {
    postgres.start();
  }

  public SessionFactory buildSessionFactory() {
    Configuration configuration = HibernateUtil.buildConfiguration();
    configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
    configuration.setProperty("hibernate.connection.username", postgres.getUsername());
    configuration.setProperty("hibernate.connection.password", postgres.getPassword());

    return configuration.buildSessionFactory();
  }

}
