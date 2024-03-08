package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Project;
import su.itpro.repository.ProjectRepository;

public class ProjectIT extends IntegrationBase {

  private final ProjectRepository projectRepository;

  public ProjectIT() {
    projectRepository = new ProjectRepository(session);
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
