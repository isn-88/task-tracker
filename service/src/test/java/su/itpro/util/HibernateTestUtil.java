package su.itpro.util;

import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import su.itpro.util.datasource.HibernateUtil;

@UtilityClass
public class HibernateTestUtil {

  private static final String IMAGE_NAME = "postgres:16";

  private static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>(IMAGE_NAME);

  static {
    POSTGRES.start();
  }

  public SessionFactory getSessionFactory() {
    Configuration configuration = HibernateUtil.buildConfiguration();
    configuration.setProperty("hibernate.connection.url", POSTGRES.getJdbcUrl());
    configuration.setProperty("hibernate.connection.username", POSTGRES.getUsername());
    configuration.setProperty("hibernate.connection.password", POSTGRES.getPassword());

    return configuration.buildSessionFactory();
  }
}
