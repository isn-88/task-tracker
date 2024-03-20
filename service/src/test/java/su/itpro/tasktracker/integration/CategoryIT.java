package su.itpro.tasktracker.integration;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import su.itpro.tasktracker.model.entity.Category;
import su.itpro.tasktracker.repository.CategoryRepository;

@RequiredArgsConstructor
public class CategoryIT extends IntegrationTestBase {

  private final CategoryRepository categoryRepository;
  private final EntityManager entityManager;

  @Test
  void createCategory() {
    Category category = Category.builder()
        .name("test-create")
        .build();
    categoryRepository.save(category);
    entityManager.flush();
    entityManager.clear();

    Optional<Category> actualResult = categoryRepository.findById(category.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(category);
  }

  @Test
  void readExistsCategory() {
    Category category1 = Category.builder()
        .name("test-read")
        .build();
    Category category2 = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category1);
    categoryRepository.save(category2);
    entityManager.flush();
    entityManager.clear();

    Optional<Category> actualResult = categoryRepository.findById(category1.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(category1);
  }

  @Test
  void readNotExistsCategory() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category);
    entityManager.flush();
    entityManager.clear();

    Optional<Category> actualResult = categoryRepository.findById(Integer.MAX_VALUE);

    assertThat(actualResult).isEmpty();
  }

  @Test
  void updateCategory() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category);
    entityManager.flush();
    entityManager.clear();
    category.setName("test-updated");
    categoryRepository.update(category);
    entityManager.flush();
    entityManager.clear();

    Optional<Category> actualResult = categoryRepository.findById(category.getId());

    assertThat(actualResult).isPresent();
    assertThat(actualResult.get()).isEqualTo(category);
  }

  @Test
  void deleteCategory() {
    Category category = Category.builder()
        .name("test-exist")
        .build();
    categoryRepository.save(category);
    entityManager.flush();

    categoryRepository.delete(category);
    entityManager.flush();

    Optional<Category> actualResult = categoryRepository.findById(category.getId());
    assertThat(actualResult).isEmpty();
  }

  @Test
  void findAll() {
    Category category1 = Category.builder()
        .name("first")
        .build();
    Category category2 = Category.builder()
        .name("second")
        .build();
    categoryRepository.save(category1);
    categoryRepository.save(category2);
    entityManager.flush();
    entityManager.clear();

    List<Category> actualResult = categoryRepository.findAll();

    assertThat(actualResult).hasSize(2);
    assertThat(actualResult).containsExactlyInAnyOrderElementsOf(List.of(category1, category2));
  }

}
