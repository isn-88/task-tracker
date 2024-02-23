package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Task;
import su.itpro.model.enums.Priority;
import su.itpro.model.enums.Status;
import su.itpro.util.HibernateTestUtil;

public class TaskIT {

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
    Task task = Task.builder()
        .title("title-create")
        .status(Status.NEW)
        .priority(Priority.NORMAL)
        .build();
    session.persist(task);
    session.flush();
    session.evict(task);

    Task actualResult = session.get(Task.class, task.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(task);
  }

  @Test
  void readExistsAccount() {
    Task task1 = Task.builder()
        .title("title-exist-1")
        .status(Status.NEW)
        .priority(Priority.NORMAL)
        .build();
    Task task2 = Task.builder()
        .title("title-exist-2")
        .status(Status.NEW)
        .priority(Priority.NORMAL)
        .build();
    session.persist(task1);
    session.persist(task2);
    session.flush();
    session.evict(task1);
    session.evict(task2);

    Task actualResult = session.get(Task.class, task1.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult).isEqualTo(task1);
  }

  @Test
  void readNotExistsAccount() {
    Task task = Task.builder()
        .title("title-not-exist")
        .status(Status.NEW)
        .priority(Priority.NORMAL)
        .build();
    session.persist(task);
    session.flush();
    session.evict(task);

    Task actualResult = session.get(Task.class, Long.MAX_VALUE);

    assertThat(actualResult).isNull();
  }

  @Test
  void updateAccount() {
    Task task = Task.builder()
        .title("title-update")
        .status(Status.NEW)
        .priority(Priority.NORMAL)
        .build();
    session.persist(task);
    task.setTitle("updated");
    session.flush();
    session.evict(task);

    Task actualResult = session.get(Task.class, task.getId());

    assertThat(actualResult).isNotNull();
    assertThat(actualResult.getId()).isEqualTo(task.getId());
    assertThat(actualResult.getTitle()).isEqualTo(task.getTitle());
  }

  @Test
  void deleteAccount() {
    Task task = Task.builder()
        .title("title-delete")
        .status(Status.NEW)
        .priority(Priority.NORMAL)
        .build();
    session.persist(task);
    session.flush();

    session.remove(task);
    session.flush();

    Task actualResult = session.get(Task.class, task.getId());
    assertThat(actualResult).isNull();
  }

}
