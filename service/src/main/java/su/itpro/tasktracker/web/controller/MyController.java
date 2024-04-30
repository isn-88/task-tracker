package su.itpro.tasktracker.web.controller;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.AccountWithGroupsDto;
import su.itpro.tasktracker.service.AccountService;
import su.itpro.tasktracker.service.TaskService;

@Controller
@RequestMapping("/my")
@RequiredArgsConstructor
public class MyController {

  private final AccountService accountService;
  private final TaskService taskService;

  @GetMapping
  public String myPage(Principal principal, Model model) {
    AccountWithGroupsDto accountWithGroups =
        accountService.findAccountWithGroups(principal.getName());
    model.addAttribute("tasks", taskService.findAllAssignedTask(accountWithGroups));
    return "my/page";
  }

}
