package su.itpro.tasktracker.repository;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Group;

@Repository
public class GroupRepository extends BaseRepository<UUID, Group> {

  public GroupRepository(EntityManager entityManager) {
    super(entityManager, Group.class);
  }

}
