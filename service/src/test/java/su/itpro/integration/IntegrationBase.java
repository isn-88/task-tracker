package su.itpro.integration;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import su.itpro.config.DaoTestConfiguration;

public abstract class IntegrationBase {

  protected static AnnotationConfigApplicationContext context;

  protected static SessionFactory sessionFactory;

  protected static EntityManager entityManager;

  @BeforeAll
  static void baseInit() {
    context = new AnnotationConfigApplicationContext(DaoTestConfiguration.class);
    sessionFactory = context.getBean(SessionFactory.class);
    entityManager = context.getBean(EntityManager.class);
  }

  @AfterAll
  static void baseDestroy() {
    sessionFactory.close();
    context.close();
  }

  @BeforeEach
  void prepare() {
    entityManager.getTransaction().begin();
  }

  @AfterEach
  void clean() {
    entityManager.getTransaction().rollback();
  }

}
