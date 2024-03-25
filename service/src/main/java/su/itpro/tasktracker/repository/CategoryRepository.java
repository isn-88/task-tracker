package su.itpro.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
