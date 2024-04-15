package su.itpro.tasktracker.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.model.dto.RegistrationDto;
import su.itpro.tasktracker.service.AccountService;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {

  private final AccountService accountService;

  @GetMapping()
  public String getPage() {
    return "login/registration";
  }

  @PostMapping()
  public String registration(RegistrationDto registrationDto) {
    accountService.registration(registrationDto);
    return "redirect:/login";
  }

}
