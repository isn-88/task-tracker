package su.itpro.tasktracker.web.controller;

import jakarta.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.AccountAboutDto;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.service.GroupService;

@Controller
@RequestMapping("/admin/team")
@RequiredArgsConstructor
public class AdminTeamController {

  private final GroupService groupService;

  @GetMapping
  public String getTeamPage(Integer groupId, Model model) {
    List<GroupReadDto> groups = groupService.findAll();
    if (groupId != null && groupId > 0) {
      GroupReadDto groupReadDto = groupService.findById(groupId);
      List<AccountAboutDto> accounts = groupService.findAccountsByGroupId(groupId);
      model.addAttribute("selectedGroup", groupReadDto);
      model.addAttribute("accounts", accounts);
    }
    model.addAttribute("groups", groups);
    model.addAttribute("tab", "team");
    return "admin/team";
  }

  @PostMapping("/exit")
  public String accountExitGroup(@NotNull Long accountId,
                                 @NotNull Integer groupId) {
    groupService.exitFromGroup(accountId, groupId);

    return "redirect:/admin/team";
  }

  @PostMapping("/group")
  public String accountUpdateGroup(@NotNull Long accountId,
                                   String[] formGroups) {
    List<Integer> groupIds = Arrays.stream(formGroups)
        .map(Integer::parseInt)
        .toList();
    groupService.updateAccountGroups(accountId, groupIds);
    return "redirect:/admin/account/find";
  }

}
