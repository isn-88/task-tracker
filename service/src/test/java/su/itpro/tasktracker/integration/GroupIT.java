package su.itpro.tasktracker.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.repository.GroupRepository;

@RequiredArgsConstructor
public class GroupIT extends IntegrationTestBase {

  private final GroupRepository groupRepository;
  private final EntityManager entityManager;

  @Test
  void createGroup() {
    Group group = Group.builder()
        .name("test-create")
        .build();
    groupRepository.save(group);
    entityManager.flush();
    entityManager.clear();

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
    entityManager.flush();
    entityManager.clear();

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
    entityManager.flush();
    entityManager.clear();

    Optional<Group> actualResult = groupRepository.findById(UUID.randomUUID());

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateGroup() {
    Group group = Group.builder()
        .name("test-update")
        .build();
    groupRepository.save(group);
    entityManager.flush();
    entityManager.clear();
    group.setName("updated");
    groupRepository.update(group);
    entityManager.flush();
    entityManager.clear();

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
    entityManager.flush();

    groupRepository.delete(group);

    Optional<Group> actualResult = groupRepository.findById(group.getId());
    assertThat(actualResult).isEmpty();
  }

}
