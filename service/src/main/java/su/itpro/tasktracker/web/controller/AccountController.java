package su.itpro.tasktracker.web.controller;

import static su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields.repeatPassword;

import jakarta.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
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
import su.itpro.tasktracker.exception.PasswordMismatchException;
import su.itpro.tasktracker.model.dto.AccountUpdateDto;
import su.itpro.tasktracker.model.dto.PasswordUpdateDto;
import su.itpro.tasktracker.model.dto.ProfileUpdateDto;
import su.itpro.tasktracker.service.AccountService;

@Slf4j
@Controller
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;
  private final MessageSource messageSource;

  @GetMapping("/edit")
  public String accountEditPage(Principal principal, Model model) {
    model.addAttribute("account",
                       accountService.findAccountByUsername(principal.getName()));
    return "account/edit";
  }

  @PostMapping
  public String updateAccount(Principal principal,
                              @Validated @ModelAttribute AccountUpdateDto updateDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("binding", bindingResult);
      redirectAttributes.addFlashAttribute("tab", "account");
      return "redirect:/account/edit";
    }

    accountService.update(updateDto, principal.getName());

    SecurityContextHolder.clearContext();
    if (session != null) {
      session.invalidate();
    }
    return "redirect:/login?change=true";
  }

  @PostMapping("/profile")
  public String updateProfile(Principal principal,
                              @Validated @ModelAttribute ProfileUpdateDto updateDto,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("binding", bindingResult);
      redirectAttributes.addFlashAttribute("tab", "profile");
      return "redirect:/account/edit";
    }
    accountService.updateProfile(updateDto, principal.getName());
    return "redirect:/account/edit";
  }

  @PostMapping("/password")
  public String updatePassword(Principal principal,
                               @Validated @ModelAttribute PasswordUpdateDto updateDto,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {
    if (bindingResult.hasErrors()) {
      redirectAttributes.addFlashAttribute("binding", clearFieldsValue(bindingResult));
      redirectAttributes.addFlashAttribute("tab", "password");
      return "redirect:/account/edit";
    }

    try {
      accountService.updatePassword(updateDto, principal.getName());
    } catch (PasswordMismatchException ex) {
      bindingResult.addError(
          new FieldError("passwordUpdateDto", ex.getField(),
                         messageSource.getMessage(ex.getMessage(), null, Locale.getDefault())
          )
      );
      redirectAttributes.addFlashAttribute("binding", clearFieldsValue(bindingResult));
      redirectAttributes.addFlashAttribute("tab", "password");
      return "redirect:/account/edit";
    }

    SecurityContextHolder.clearContext();
    if (session != null) {
      session.invalidate();
    }
    return "redirect:/login?change=true";
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
                           repeatPassword,
                           Objects.toString(error.getDefaultMessage(), "")
            )
        );
      }
    }
    return bindingResult;
  }

}
