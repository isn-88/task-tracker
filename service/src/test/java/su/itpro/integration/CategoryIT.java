package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Category;
import su.itpro.util.HibernateTestUtil;

public class CategoryIT {

  private static SessionFactory sessionFactory;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

  @BeforeEach
  void prepare() {
    session = sessionFactory.openSession();
    session.beginTransaction();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
    session.close();
  }

  @Test
  void createCategory() {
    Category category = Category.builder()
        .name("test-create")
        .build();
    session.persist(category);
    session.flush();
    session.evict(category);

    Category actualResult = session.get(Category.class, category.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(category);
  }

  @Test
  void readExistsAccount() {
    Category category1 = Category.builder()
        .name("test-read")
        .build();
    Category category2 = Category.builder()
        .name("test-exist")
        .build();
    session.persist(category1);
    session.persist(category2);
    session.flush();
    session.evict(category1);
    session.evict(category2);

    Category actualResult = session.get(Category.class, category1.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(category1);
  }

  @Test
  void readNotExistsAccount() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    session.persist(category);
    session.flush();
    session.evict(category);

    Category actualResult = session.get(Category.class, Integer.MAX_VALUE);

    assertThat(actualResult).isNull();
  }

  @Test
  void updateAccount() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    session.persist(category);
    category.setName("test-updated");
    session.flush();
    session.evict(category);

    Category actualResult = session.get(Category.class, category.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult.getId()).isEqualTo(category.getId());
    assertThat(actualResult.getName()).isEqualTo(category.getName());
  }

  @Test
  void deleteAccount() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    session.persist(category);
    session.flush();

    session.remove(category);
    session.flush();

    Category actualResult = session.get(Category.class, category.getId());
    assertThat(actualResult).isNull();
  }

}
