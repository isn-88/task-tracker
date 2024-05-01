package su.itpro.tasktracker.web.controller;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.ProjectFindDto;
import su.itpro.tasktracker.model.dto.ProjectUpdateDto;
import su.itpro.tasktracker.service.GroupService;
import su.itpro.tasktracker.service.ProjectService;

@Controller
@RequestMapping("/admin/project")
@RequiredArgsConstructor
public class AdminProjectController {

  private final ProjectService projectService;
  private final GroupService groupService;

  @GetMapping("/find")
  public String getProject(ProjectFindDto findDto, Model model) {
    Sort sort = Sort.by("name");
    model.addAttribute("filter", findDto);
    model.addAttribute("projects", projectService.findAllProjects(findDto, sort));
    model.addAttribute("tab", "project");
    if (findDto.findById() != null) {
      model.addAttribute("edit", projectService.findById(findDto.findById()).orElse(null));
      model.addAttribute("groups", groupService.findAll());
      model.addAttribute("projectGroups",
                         projectService.findAllGroupIdsByProjectId(findDto.findById()));
    }
    return "admin/project";
  }

  @PostMapping
  public String createProject(ProjectUpdateDto createDto) {
    projectService.createProject(createDto);
    return "redirect:/admin/project/find";
  }

  @PostMapping("/edit")
  public String updateProject(@Validated @ModelAttribute("edit") ProjectUpdateDto updateDto) {
    projectService.updateProject(updateDto);
    return "redirect:/admin/project/find?findById=" + updateDto.id();
  }

  @PostMapping("/group")
  public String accountUpdateGroup(@NotNull Integer projectId,
                                   String[] formGroups) {
    List<Integer> groupIds = Arrays.stream(formGroups)
        .map(Integer::parseInt)
        .toList();
    projectService.updateProjectGroups(projectId, groupIds);
    return "redirect:/admin/project/find?findById=" + projectId;
  }

}
