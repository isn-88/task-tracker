package su.itpro.tasktracker.web.controller;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.AccountWithGroupsDto;
import su.itpro.tasktracker.model.dto.ProjectReadDto;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.dto.TaskReadDto;
import su.itpro.tasktracker.service.AccountService;
import su.itpro.tasktracker.service.ProjectService;
import su.itpro.tasktracker.service.TaskService;

@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

  private final AccountService accountService;
  private final TaskService taskService;
  private final ProjectService projectService;

  @GetMapping
  public String myPage(Principal principal,
                       Model model) {
    AccountWithGroupsDto accountWithGroups =
        accountService.findAccountWithGroups(principal.getName());
    TaskFilter filter = TaskFilter.builder()
        .ownerId(accountWithGroups.account().id())
        .build();
    List<ProjectReadDto> allAccessedProjects =
        projectService.findAllAccessedProjects(accountWithGroups);
    model.addAttribute("myTasks", taskService.findAllAssignedTask(accountWithGroups));
    model.addAttribute("createdTask", taskService.findAllBy(filter));
    model.addAttribute("task", TaskReadDto.builder().build());
    model.addAttribute("projects", allAccessedProjects);
    model.addAttribute("account", accountWithGroups.account());
    model.addAttribute("allAssigned", accountService.findAllAssigned());
    return "my/page";
  }

}
