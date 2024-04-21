package su.itpro.tasktracker.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

  @GetMapping("/page")
  public String myPage(Model model,
                       @AuthenticationPrincipal UserDetails userDetails) {
    AccountWithGroupsDto accountWithGroups =
        accountService.findAccountWithGroups(userDetails.getUsername());
    model.addAttribute("tasks",
                       taskService.findAllAssignedTaskForUser(accountWithGroups));
    return "my/page";
  }

}
