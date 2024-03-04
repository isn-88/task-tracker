package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Project;
import su.itpro.repository.ProjectRepository;
import su.itpro.util.HibernateTestUtil;

public class ProjectIT {

  private static SessionFactory sessionFactory;

  private static ProjectRepository projectRepository;

  private Session session;

  @BeforeAll
  static void init() {
    sessionFactory = HibernateTestUtil.buildSessionFactory();
    Session proxySession = HibernateTestUtil.buildProxySession(sessionFactory);
    projectRepository = new ProjectRepository(proxySession);
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
  void createProject() {
    Project project = Project.builder()
        .name("test-create")
        .build();
    projectRepository.save(project);
    session.flush();
    session.clear();

    Optional<Project> actualResult = projectRepository.findById(project.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(project);
  }

  @Test
  void readExistsProject() {
    Project project1 = Project.builder()
        .name("test-exist-1")
        .build();
    Project project2 = Project.builder()
        .name("test-exist-2")
        .build();
    projectRepository.save(project1);
    projectRepository.save(project2);
    session.flush();
    session.clear();

    Optional<Project> actualResult = projectRepository.findById(project1.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(project1);
  }

  @Test
  void readNotExistsProject() {
    Project project = Project.builder()
        .name("test-not-exist")
        .build();
    projectRepository.save(project);
    session.flush();
    session.clear();

    Optional<Project> actualResult = projectRepository.findById(UUID.randomUUID());

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateProject() {
    Project project = Project.builder()
        .name("test-update")
        .build();
    projectRepository.save(project);
    session.flush();
    session.clear();
    project.setName("updated");
    projectRepository.update(project);
    session.flush();
    session.clear();

    Optional<Project> actualResult = projectRepository.findById(project.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(project);
  }

  @Test
  void deleteProject() {
    Project project = Project.builder()
        .name("test-delete")
        .build();
    projectRepository.save(project);
    session.flush();

    projectRepository.delete(project);
    session.flush();

    Optional<Project> actualResult = projectRepository.findById(project.getId());
    assertThat(actualResult).isEmpty();
  }

  @Test
  void findAllProjects() {
    Project project1 = Project.builder()
        .name("test-exist-1")
        .build();
    Project project2 = Project.builder()
        .name("test-exist-2")
        .build();
    projectRepository.save(project1);
    projectRepository.save(project2);
    session.flush();
    session.clear();

    List<Project> actualResult = projectRepository.findAll();

    assertThat(actualResult).hasSize(2);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(List.of(project1, project2));
  }
}
