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
import su.itpro.model.entity.Group;
import su.itpro.util.HibernateTestUtil;

public class GroupIT {

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
    Group group = Group.builder()
        .name("test-create")
        .build();
    session.persist(group);
    session.flush();
    session.evict(group);

    Group actualResult = session.get(Group.class, group.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(group);
  }

  @Test
  void testReadExistsAccount() {
    Group group1 = Group.builder()
        .name("test-exist-1")
        .build();
    Group group2 = Group.builder()
        .name("test-exist-2")
        .build();
    session.persist(group1);
    session.persist(group2);
    session.flush();
    session.evict(group1);
    session.evict(group2);

    Group actualResult = session.get(Group.class, group1.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(group1);
  }

  @Test
  void testReadNotExistsAccount() {
    Group group = Group.builder()
        .name("test-create")
        .build();
    session.persist(group);
    session.flush();
    session.evict(group);

    Group actualResult = session.get(Group.class, UUID.randomUUID());

    assertThat(actualResult).isNull();
  }

  @Test
  void testUpdateAccount() {
    Group group = Group.builder()
        .name("test-update")
        .build();
    session.persist(group);
    group.setName("updated");
    session.flush();
    session.evict(group);

    Group actualResult = session.get(Group.class, group.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult.getId()).isEqualTo(group.getId());
    assertThat(actualResult.getName()).isEqualTo(group.getName());
  }

  @Test
  void testDeleteAccount() {
    Group group = Group.builder()
        .name("test-delete")
        .build();
    session.persist(group);
    session.flush();

    session.remove(group);
    session.flush();

    Group actualResult = session.get(Group.class, group.getId());
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
