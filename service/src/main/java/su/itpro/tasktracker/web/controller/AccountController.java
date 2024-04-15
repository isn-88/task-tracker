package su.itpro.tasktracker.web.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import su.itpro.tasktracker.model.dto.AccountUpdateDto;
import su.itpro.tasktracker.model.dto.PasswordUpdateDto;
import su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields;
import su.itpro.tasktracker.model.dto.ProfileUpdateDto;
import su.itpro.tasktracker.service.AccountService;

@Slf4j
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @GetMapping
  public String accountPage(Model model,
                            @AuthenticationPrincipal UserDetails userDetails) {
    model.addAttribute("account",
                       accountService.findAccountByUsername(userDetails.getUsername()));

    return "account/account";
  }

  @PostMapping
  public String updateAccount(@AuthenticationPrincipal UserDetails userDetails,
                              @Validated @ModelAttribute AccountUpdateDto updateDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("binding", bindingResult);
      redirectAttributes.addFlashAttribute("tab", "account");
      return "redirect:/account";
    }

    accountService.update(updateDto, userDetails.getUsername());
    return "redirect:/logout";
  }

  @PostMapping("/profile")
  public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                              @Validated @ModelAttribute ProfileUpdateDto updateDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("binding", bindingResult);
      redirectAttributes.addFlashAttribute("tab", "profile");
      return "redirect:/account";
    }
    accountService.updateProfile(updateDto, userDetails.getUsername());
    return "redirect:/account";
  }

  @PostMapping("/password")
  public String updatePassword(@AuthenticationPrincipal UserDetails userDetails,
                               @Validated @ModelAttribute PasswordUpdateDto updateDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("binding", clearFieldsValue(bindingResult));
      redirectAttributes.addFlashAttribute("tab", "password");
      return "redirect:/account";
    }

    accountService.updatePassword(updateDto, userDetails.getUsername());

    return "redirect:/logout";
  }


  private BindingResult clearFieldsValue(BindingResult bindingSource) {
    BindingResult bindingResult =
        new BeanPropertyBindingResult(PasswordUpdateDto.class, "passwordUpdateDto");
    for (FieldError error : bindingSource.getFieldErrors()) {
      bindingResult.addError(
          new FieldError(error.getObjectName(),
                         error.getField(),
                         Objects.toString(error.getDefaultMessage(), "")
          )
      );
    }
    for (ObjectError error : bindingSource.getAllErrors()) {
      if (error.getCode() != null && error.getCode().equals("PasswordCheckRepeat")) {
        bindingResult.addError(
            new FieldError(error.getObjectName(),
                           Fields.repeatPassword,
                           Objects.toString(error.getDefaultMessage(), "")
            )
        );
      }
    }
    return bindingResult;
  }

}
