package su.itpro.tasktracker.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import su.itpro.tasktracker.service.AccountService;

@Controller
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountAboutController {

  private final AccountService accountService;

  @GetMapping("/{id}")
  public String getAccountAboutPage(@PathVariable Long id, Model model) {
    model.addAttribute("account", accountService.findAccountAboutBy(id));
    return "account/about";
  }

}
