package su.itpro.config;

import jakarta.persistence.EntityManager;
import java.lang.reflect.Proxy;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import su.itpro.util.HibernateTestUtil;

@Configuration
@ComponentScan(basePackages = "su.itpro")
public class DaoTestConfiguration {

  @Bean(destroyMethod = "close")
  public SessionFactory getSessionFactory() {
    return HibernateTestUtil.buildSessionFactory();
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
