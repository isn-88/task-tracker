package su.itpro.tasktracker.web.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.model.dto.GroupUpdateDto;
import su.itpro.tasktracker.service.GroupService;

@Controller
@RequestMapping("/admin/group")
@RequiredArgsConstructor
public class AdminGroupController {

  private final GroupService groupService;

  @GetMapping
  public String getGroup(Model model) {
    List<GroupReadDto> groups = groupService.findAllGroupsWithCount();
    model.addAttribute("groups", groups);
    model.addAttribute("tab", "group");
    return "admin/group";
  }

  @PostMapping
  public String createGroup(GroupUpdateDto createDto) {
    groupService.createGroup(createDto);
    return "redirect:/admin/group";
  }

  @PostMapping("/edit")
  public String editGroup(GroupUpdateDto updateDto) {
    groupService.updateGroup(updateDto);
    return "redirect:/admin/group";
  }

  @PostMapping("/delete")
  public String deleteGroup(GroupUpdateDto updateDto) {
    groupService.deleteGroup(updateDto);
    return "redirect:/admin/group";
  }

}
