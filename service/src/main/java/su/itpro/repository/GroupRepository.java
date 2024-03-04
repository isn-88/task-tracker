package su.itpro.repository;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import su.itpro.model.entity.Group;

public class GroupRepository extends BaseRepository<UUID, Group> {

  public GroupRepository(EntityManager entityManager) {
    super(entityManager, Group.class);
  }

}
