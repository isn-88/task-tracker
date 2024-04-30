package su.itpro.tasktracker.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.ProjectCreateDto;
import su.itpro.tasktracker.service.ProjectService;

@Controller
@RequestMapping("/admin/project")
@RequiredArgsConstructor
public class AdminProjectController {

  private final ProjectService projectService;

  @GetMapping
  public String getProject(Model model) {
    model.addAttribute("projects", projectService.findAll());
    model.addAttribute("tab", "project");
    return "admin/project";
  }

  @PostMapping
  public String createProject(ProjectCreateDto createDto) {
    projectService.createProject(createDto);
    return "redirect:/admin/project";
  }

}
