package su.itpro.integration;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import su.itpro.util.HibernateTestUtil;

public class IntegrationBase {

  protected static SessionFactory sessionFactory;

  protected static Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
    session = HibernateTestUtil.buildProxySession(sessionFactory);
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

  @BeforeEach
  void prepare() {
    session.beginTransaction();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
  }

}
