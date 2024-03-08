package su.itpro.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import su.itpro.model.entity.Group;
import su.itpro.repository.GroupRepository;

public class GroupIT extends IntegrationBase {

  private final GroupRepository groupRepository;

  public GroupIT() {
    groupRepository = new GroupRepository(session);
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
