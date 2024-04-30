package su.itpro.tasktracker.web.controller;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.dto.TaskReadDto;
import su.itpro.tasktracker.model.dto.TaskUpdateDto;
import su.itpro.tasktracker.service.AccountService;
import su.itpro.tasktracker.service.ProjectService;
import su.itpro.tasktracker.service.TaskService;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

  private final AccountService accountService;
  private final ProjectService projectService;
  private final TaskService taskService;

  @GetMapping
  public String findAll(TaskFilter filter, Model model) {
    List<TaskReadDto> tasks = taskService.findAllBy(filter);
    model.addAttribute("tasks", tasks);
    return "task/tasks";
  }

  @GetMapping("/{id}")
  public String findById(@PathVariable Long id, Model model) {
    TaskReadDto taskReadDto = taskService.findById(id)
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
    model.addAttribute("task", taskReadDto);
    model.addAttribute("projects", projectService.findAll());
    model.addAttribute("allAssigned", accountService.findAllAssigned());
    return "task/task";
  }

  @PostMapping
  public String create(@Validated TaskUpdateDto taskCreateDto) {
    return "redirect:/tasks/" + taskService.create(taskCreateDto).id();
  }

  @PostMapping("/{id}/update")
  public String update(@PathVariable Long id,
                       @Validated TaskUpdateDto taskCreateDto,
                       BindingResult bindingResult,
                       RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
      return "redirect:/tasks/{id}";
    }
    return taskService.update(id, taskCreateDto)
        .map(it -> "redirect:/tasks/{id}")
        .orElseThrow(() -> new ResponseStatusException(NOT_FOUND));
  }

}
