package su.itpro.repository;

import static su.itpro.model.entity.QProject.project;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import su.itpro.model.entity.Project;

@Repository
public class ProjectRepository extends BaseRepository<UUID, Project> {

  public ProjectRepository(EntityManager entityManager) {
    super(entityManager, Project.class);
  }

  public List<Project> findAll() {
    return new JPAQuery<Project>(entityManager)
        .select(project)
        .from(project)
        .fetch();
  }

}
