package su.itpro.tasktracker.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dto.CategoryReadDto;
import su.itpro.tasktracker.model.dto.CategoryUpdateDto;
import su.itpro.tasktracker.model.mapper.CategoryReadMapper;
import su.itpro.tasktracker.model.mapper.CategoryUpdateMapper;
import su.itpro.tasktracker.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryUpdateMapper categoryUpdateMapper;
  private final CategoryReadMapper categoryReadMapper;

  @Transactional(readOnly = true)
  public List<CategoryReadDto> findAll() {
    return categoryRepository.findAll().stream()
        .map(categoryReadMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public Optional<CategoryReadDto> findById(Short id) {
    return categoryRepository.findById(id)
        .map(categoryReadMapper::map);
  }

  public CategoryReadDto create(CategoryUpdateDto categoryCreateDto) {
    return Optional.of(categoryCreateDto)
        .map(categoryUpdateMapper::map)
        .map(categoryRepository::saveAndFlush)
        .map(categoryReadMapper::map)
        .orElseThrow();
  }

  public Optional<CategoryReadDto> update(Short id, CategoryUpdateDto categoryUpdateDto) {
    return categoryRepository.findById(id)
        .map(category -> categoryUpdateMapper.map(categoryUpdateDto, category))
        .map(categoryRepository::saveAndFlush)
        .map(categoryReadMapper::map);
  }

}
