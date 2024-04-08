package su.itpro.tasktracker.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dto.CategoryCreateUpdateDto;
import su.itpro.tasktracker.model.dto.CategoryReadDto;
import su.itpro.tasktracker.model.mapper.CategoryCreateUpdateMapper;
import su.itpro.tasktracker.model.mapper.CategoryReadMapper;
import su.itpro.tasktracker.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryCreateUpdateMapper categoryCreateUpdateMapper;
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

  public CategoryReadDto create(CategoryCreateUpdateDto categoryCreateDto) {
    return Optional.of(categoryCreateDto)
        .map(categoryCreateUpdateMapper::map)
        .map(categoryRepository::saveAndFlush)
        .map(categoryReadMapper::map)
        .orElseThrow();
  }

  public Optional<CategoryReadDto> update(Short id, CategoryCreateUpdateDto categoryUpdateDto) {
    return categoryRepository.findById(id)
        .map(category -> categoryCreateUpdateMapper.map(categoryUpdateDto, category))
        .map(categoryRepository::saveAndFlush)
        .map(categoryReadMapper::map);
  }

}