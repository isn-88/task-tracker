package su.itpro.util;

import java.lang.reflect.Proxy;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import su.itpro.util.datasource.HibernateUtil;

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

  public Session buildProxySession(SessionFactory sessionFactory) {
    return (Session) Proxy.newProxyInstance(
        SessionFactory.class.getClassLoader(),
        new Class[]{Session.class},
        (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args)
    );
  }
}
