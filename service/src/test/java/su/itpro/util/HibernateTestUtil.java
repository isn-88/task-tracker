package su.itpro.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import su.itpro.util.datasource.HibernateUtil;

@UtilityClass
public class HibernateTestUtil {

  private static final String IMAGE_NAME = "postgres:16";

  private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(IMAGE_NAME);

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
