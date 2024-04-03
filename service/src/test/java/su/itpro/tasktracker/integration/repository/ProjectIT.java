package su.itpro.tasktracker.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.integration.IntegrationTestBase;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.repository.ProjectRepository;

@RequiredArgsConstructor
public class ProjectIT extends IntegrationTestBase {

  private final ProjectRepository projectRepository;
  private final EntityManager entityManager;

  @Test
  void createProject() {
    Project project = Project.builder()
        .name("test-create")
        .build();
    projectRepository.save(project);
    entityManager.flush();
    entityManager.clear();

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
    entityManager.flush();
    entityManager.clear();

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
    entityManager.flush();
    entityManager.clear();

    Optional<Project> actualResult = projectRepository.findById(Integer.MAX_VALUE);

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateProject() {
    Project project = Project.builder()
        .name("test-update")
        .build();
    projectRepository.save(project);
    entityManager.flush();
    entityManager.clear();
    project.setName("updated");
    projectRepository.save(project);
    entityManager.flush();
    entityManager.clear();

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
    entityManager.flush();

    projectRepository.delete(project);
    entityManager.flush();

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
    entityManager.flush();
    entityManager.clear();

    List<Project> actualResult = projectRepository.findAll();

    assertThat(actualResult).hasSize(2);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(List.of(project1, project2));
  }
}
