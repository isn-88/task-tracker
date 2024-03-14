package su.itpro.tasktracker.config;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import su.itpro.tasktracker.util.HibernateTestUtil;

@Configuration
@ComponentScan(basePackages = "su.itpro.tasktracker")
public class RepositoryTestConfiguration {

  @Bean(destroyMethod = "close")
  public SessionFactory getSessionFactory() {
    return HibernateTestUtil.buildSessionFactory();
  }

}
