package su.itpro.repository;

import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import su.itpro.model.entity.Profile;

@Repository
public class ProfileRepository extends BaseRepository<UUID, Profile> {

  public ProfileRepository(EntityManager entityManager) {
    super(entityManager, Profile.class);
  }

}
