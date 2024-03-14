package su.itpro.tasktracker.integration;

import jakarta.persistence.EntityManager;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import su.itpro.tasktracker.config.RepositoryTestConfiguration;

public abstract class IntegrationBase {

  protected static AnnotationConfigApplicationContext context;

  protected static SessionFactory sessionFactory;

  protected static EntityManager entityManager;

  @BeforeAll
  static void baseInit() {
    context = new AnnotationConfigApplicationContext(RepositoryTestConfiguration.class);
    sessionFactory = context.getBean(SessionFactory.class);
    entityManager = context.getBean(EntityManager.class);
  }

  @AfterAll
  static void baseDestroy() {
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
