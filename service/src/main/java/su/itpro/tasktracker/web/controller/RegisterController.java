package su.itpro.tasktracker.web.controller;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.servlet.LocaleResolver;
import su.itpro.tasktracker.model.dto.RegisterDto;
import su.itpro.tasktracker.service.AccountService;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegisterController {

  private final AccountService accountService;
  private final LocaleResolver localeResolver;

  @GetMapping()
  public String getPage(Model model) {
    model.addAttribute("register", RegisterDto.builder().build());
    return "login/registration";
  }

  @PostMapping()
  public String register(@Validated @ModelAttribute("register")
                         RegisterDto registerDto,
                         BindingResult bindingResult,
                         HttpServletRequest request) {
    accountService.register(registerDto, bindingResult, localeResolver.resolveLocale(request));
    if (bindingResult.hasErrors()) {
      return "login/registration";
    }
    return "redirect:/login";
  }

  @InitBinder
  public void initBinder(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

}
