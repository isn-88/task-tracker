package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Category;
import su.itpro.repository.CategoryRepository;
import su.itpro.util.HibernateTestUtil;

public class CategoryIT {

  private static SessionFactory sessionFactory;

  private static CategoryRepository categoryRepository;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
    Session proxySession = HibernateTestUtil.buildProxySession(sessionFactory);
    categoryRepository = new CategoryRepository(proxySession);
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

  @BeforeEach
  void prepare() {
    session = sessionFactory.getCurrentSession();
    session.beginTransaction();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
  }

  @Test
  void createCategory() {
    Category category = Category.builder()
        .name("test-create")
        .build();
    categoryRepository.save(category);
    session.flush();
    session.clear();

    Optional<Category> actualResult = categoryRepository.findById(category.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(category);
  }

  @Test
  void readExistsCategory() {
    Category category1 = Category.builder()
        .name("test-read")
        .build();
    Category category2 = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category1);
    categoryRepository.save(category2);
    session.flush();
    session.clear();

    Optional<Category> actualResult = categoryRepository.findById(category1.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(category1);
  }

  @Test
  void readNotExistsCategory() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category);
    session.flush();
    session.clear();

    Optional<Category> actualResult = categoryRepository.findById(Integer.MAX_VALUE);

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateCategory() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category);
    session.flush();
    session.clear();
    category.setName("test-updated");
    categoryRepository.update(category);
    session.flush();
    session.clear();

    Optional<Category> actualResult = categoryRepository.findById(category.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(category);
  }

  @Test
  void deleteCategory() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category);
    session.flush();

    categoryRepository.delete(category);
    session.flush();

    Optional<Category> actualResult = categoryRepository.findById(category.getId());
    assertThat(actualResult).isEmpty();
  }

  @Test
  void findAll() {
    Category category1 = Category.builder()
        .name("first")
        .build();
    Category category2 = Category.builder()
        .name("second")
        .build();
    categoryRepository.save(category1);
    categoryRepository.save(category2);
    session.flush();
    session.clear();

    List<Category> actualResult = categoryRepository.findAll();

    assertThat(actualResult).hasSize(2);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(List.of(category1, category2));
  }

}
