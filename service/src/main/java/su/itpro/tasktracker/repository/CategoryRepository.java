package su.itpro.tasktracker.repository;

import static su.itpro.tasktracker.model.entity.QCategory.category;

import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Category;

@Repository
public class CategoryRepository extends BaseRepository<Integer, Category> {

  public CategoryRepository(EntityManager entityManager) {
    super(entityManager, Category.class);
  }

  public List<Category> findAll() {
    return new JPAQuery<Category>(entityManager)
        .select(category)
        .from(category)
        .fetch();
  }
}
