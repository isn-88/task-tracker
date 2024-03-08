package su.itpro.repository;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import su.itpro.model.entity.Profile;

public class ProfileRepository extends BaseRepository<UUID, Profile> {

  public ProfileRepository(EntityManager entityManager) {
    super(entityManager, Profile.class);
  }

}
