package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Group;
import su.itpro.repository.GroupRepository;
import su.itpro.util.HibernateTestUtil;

public class GroupIT {

  private static SessionFactory sessionFactory;

  private static GroupRepository groupRepository;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
    Session proxySession = HibernateTestUtil.buildProxySession(sessionFactory);
    groupRepository = new GroupRepository(proxySession);
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
  void createGroup() {
    Group group = Group.builder()
        .name("test-create")
        .build();
    groupRepository.save(group);
    session.flush();
    session.clear();

    Optional<Group> actualResult = groupRepository.findById(group.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(group);
  }

  @Test
  void readExistsGroup() {
    Group group1 = Group.builder()
        .name("test-exist-1")
        .build();
    Group group2 = Group.builder()
        .name("test-exist-2")
        .build();
    groupRepository.save(group1);
    groupRepository.save(group2);
    session.flush();
    session.clear();

    Optional<Group> actualResult = groupRepository.findById(group1.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(group1);
  }

  @Test
  void readNotExistsGroup() {
    Group group = Group.builder()
        .name("test-create")
        .build();
    groupRepository.save(group);
    session.flush();
    session.clear();

    Optional<Group> actualResult = groupRepository.findById(UUID.randomUUID());

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateGroup() {
    Group group = Group.builder()
        .name("test-update")
        .build();
    groupRepository.save(group);
    session.flush();
    session.clear();
    group.setName("updated");
    groupRepository.update(group);
    session.flush();
    session.clear();

    Optional<Group> actualResult = groupRepository.findById(group.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(group);
  }

  @Test
  void deleteGroup() {
    Group group = Group.builder()
        .name("test-delete")
        .build();
    groupRepository.save(group);
    session.flush();

    groupRepository.delete(group);

    Optional<Group> actualResult = groupRepository.findById(group.getId());
    assertThat(actualResult).isEmpty();
  }

}
