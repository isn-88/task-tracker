package su.itpro.tasktracker.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public String login(@RequestParam(name = "register", defaultValue = "false") Boolean isRegister,
                      Model model) {
    model.addAttribute("register", isRegister);
    return "login/login";
  }

}
