package su.itpro.tasktracker.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
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
  public String getPage(Model model) {
    model.addAttribute("register", RegistrationDto.builder().build());
    return "login/registration";
  }

  @PostMapping()
  public String registration(@Validated @ModelAttribute("register")
                             RegistrationDto registrationDto,
                             BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "login/registration";
    }
    accountService.register(registrationDto);
    return "redirect:/login";
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

}
