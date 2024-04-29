package su.itpro.tasktracker.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.GroupCreateDto;
import su.itpro.tasktracker.service.GroupService;

@Controller
@RequestMapping("/admin/group")
@RequiredArgsConstructor
public class AdminGroupController {

  private final GroupService groupService;

  @GetMapping
  public String getGroup(Model model) {
    model.addAttribute("groups", groupService.findAll());
    model.addAttribute("tab", "group");
    return "admin/group";
  }

  @PostMapping
  public String createGroup(GroupCreateDto createDto) {
    groupService.createGroup(createDto);
    return "redirect:/admin/group";
  }

}
