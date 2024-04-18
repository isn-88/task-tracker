package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.CategoryUpdateDto;
import su.itpro.tasktracker.model.entity.Category;

@Component
public class CategoryCreateUpdateMapper implements Mapper<CategoryUpdateDto, Category> {

  @Override
  public Category map(CategoryUpdateDto dto) {
    return copy(dto, new Category());
  }

  @Override
  public Category map(CategoryUpdateDto dto, Category category) {
    return copy(dto, category);
  }

  private Category copy(CategoryUpdateDto dto, Category category) {
    category.setName(dto.name());
    return category;
  }
}
