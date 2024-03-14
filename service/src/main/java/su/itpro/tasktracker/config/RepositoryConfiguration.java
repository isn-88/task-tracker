package su.itpro.tasktracker.config;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Proxy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import su.itpro.tasktracker.util.datasource.HibernateUtil;

@Configuration
@PropertySource("classpath:hibernate.properties")
public class RepositoryConfiguration {

  @Bean(destroyMethod = "close")
  public SessionFactory getSessionFactory() {
    return HibernateUtil.buildSessionFactory();
  }

  @Bean
  public EntityManager buildProxySession(SessionFactory sessionFactory) {
    return (EntityManager) Proxy.newProxyInstance(
        SessionFactory.class.getClassLoader(),
        new Class[]{Session.class},
        (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args)
    );
  }

}
