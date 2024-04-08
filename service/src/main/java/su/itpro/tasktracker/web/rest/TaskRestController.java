package su.itpro.tasktracker.web.rest;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.dto.TaskReadDto;
import su.itpro.tasktracker.service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskRestController {

  private final TaskService taskService;

  @GetMapping
  public ResponseEntity<List<TaskReadDto>> findAll(TaskFilter filter) {
    return ResponseEntity.ok(taskService.findAllBy(filter));
  }

}
