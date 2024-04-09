package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.CategoryCreateUpdateDto;
import su.itpro.tasktracker.model.entity.Category;

@Component
public class CategoryCreateUpdateMapper implements Mapper<CategoryCreateUpdateDto, Category> {

  @Override
  public Category map(CategoryCreateUpdateDto dto) {
    return copy(dto, new Category());
  }

  @Override
  public Category map(CategoryCreateUpdateDto updateDto, Category category) {
    return copy(updateDto, category);
  }

  private Category copy(CategoryCreateUpdateDto dto, Category category) {
    category.setName(dto.name());
    return category;
  }
}
