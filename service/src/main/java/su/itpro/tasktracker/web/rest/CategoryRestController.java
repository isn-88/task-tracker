package su.itpro.tasktracker.web.rest;

import static org.springframework.http.HttpStatus.CREATED;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import su.itpro.tasktracker.model.dto.CategoryCreateUpdateDto;
import su.itpro.tasktracker.model.dto.CategoryReadDto;
import su.itpro.tasktracker.service.CategoryService;
import su.itpro.tasktracker.web.exception.ResourceNotFoundException;
import su.itpro.tasktracker.web.response.RestResponse;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryRestController {

  private final CategoryService categoryService;

  @GetMapping
  public RestResponse<List<CategoryReadDto>> findAll() {
    return RestResponse.success(categoryService.findAll());
  }

  @GetMapping("/{id}")
  public RestResponse<CategoryReadDto> findById(@PathVariable Short id) {
    return RestResponse.success(categoryService.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                        "Category with id: " + id + " not found")));
  }

  @PostMapping
  @ResponseStatus(CREATED)
  public RestResponse<CategoryReadDto> create(@Validated @RequestBody
                                              CategoryCreateUpdateDto createDto) {
    return RestResponse.success(categoryService.create(createDto));
  }

  @PutMapping("/{id}")
  public RestResponse<CategoryReadDto> update(@PathVariable("id") Short id,
                                              @Validated @RequestBody
                                              CategoryCreateUpdateDto updateDto) {
    return RestResponse.success(categoryService.update(id, updateDto)
                                    .orElseThrow(() -> new ResourceNotFoundException(
                                        "Category with id: " + id + " not found")));
  }

}
