package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.CategoryReadDto;
import su.itpro.tasktracker.model.entity.Category;

@Component
public class CategoryReadMapper implements Mapper<Category, CategoryReadDto> {

  @Override
  public CategoryReadDto map(Category category) {
    return CategoryReadDto.builder()
        .id(category.getId())
        .name(category.getName())
        .build();
  }
}
