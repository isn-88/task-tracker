package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Project;
import su.itpro.util.HibernateTestUtil;

public class ProjectIT {

  private static SessionFactory sessionFactory;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.getSessionFactory();
  }

  @BeforeEach
  void prepare() {
    session = sessionFactory.openSession();
    session.beginTransaction();
  }

  @Test
  void testCreateCategory() {
    Project project = Project.builder()
        .name("test-create")
        .build();
    session.persist(project);
    session.flush();
    session.evict(project);

    Project actualResult = session.get(Project.class, project.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(project);
  }

  @Test
  void testReadExistsAccount() {
    Project project1 = Project.builder()
        .name("test-exist-1")
        .build();
    Project project2 = Project.builder()
        .name("test-exist-2")
        .build();
    session.persist(project1);
    session.persist(project2);
    session.flush();
    session.evict(project1);
    session.evict(project2);

    Project actualResult = session.get(Project.class, project1.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(project1);
  }

  @Test
  void testReadNotExistsAccount() {
    Project project = Project.builder()
        .name("test-not-exist")
        .build();
    session.persist(project);
    session.flush();
    session.evict(project);

    Project actualResult = session.get(Project.class, UUID.randomUUID());

    assertThat(actualResult).isNull();
  }

  @Test
  void testUpdateAccount() {
    Project project = Project.builder()
        .name("test-update")
        .build();
    session.persist(project);
    project.setName("updated");
    session.flush();
    session.evict(project);

    Project actualResult = session.get(Project.class, project.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult.getId()).isEqualTo(project.getId());
    assertThat(actualResult.getName()).isEqualTo(project.getName());
  }

  @Test
  void testDeleteAccount() {
    Project project = Project.builder()
        .name("test-delete")
        .build();
    session.persist(project);
    session.flush();

    session.remove(project);
    session.flush();

    Project actualResult = session.get(Project.class, project.getId());
    assertThat(actualResult).isNull();
  }

  @AfterEach
  void clean() {
    session.getTransaction().rollback();
    session.close();
  }

  @AfterAll
  static void destroy() {
    sessionFactory.close();
  }

}
