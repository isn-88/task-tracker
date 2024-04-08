package su.itpro.tasktracker.web.rest;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.itpro.tasktracker.model.dto.CategoryCreateUpdateDto;
import su.itpro.tasktracker.model.dto.CategoryReadDto;
import su.itpro.tasktracker.service.CategoryService;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

  private final CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<CategoryReadDto>> findAll() {
    return ResponseEntity.ok(categoryService.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryReadDto> findById(@PathVariable Short id) {
    return categoryService.findById(id)
        .map(ResponseEntity::ok)
        .orElseGet(ResponseEntity.notFound()::build);
  }

  @PostMapping
  public ResponseEntity<CategoryReadDto> create(@Validated @RequestBody
                                                CategoryCreateUpdateDto createDto) {
    return ResponseEntity.status(CREATED)
        .body(categoryService.create(createDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryReadDto> update(@PathVariable("id") Short id,
                                                @Validated @RequestBody
                                                CategoryCreateUpdateDto updateDto) {
    return categoryService.update(id, updateDto)
        .map(ResponseEntity::ok)
        .orElseGet(ResponseEntity.notFound()::build);
  }

}
