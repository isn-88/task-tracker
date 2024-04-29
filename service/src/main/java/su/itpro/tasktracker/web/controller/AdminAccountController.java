package su.itpro.tasktracker.web.controller;

import java.security.Principal;
import java.util.Locale;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import su.itpro.tasktracker.model.dto.AccountBlockDto;
import su.itpro.tasktracker.model.dto.AccountFilterFormDto;
import su.itpro.tasktracker.model.dto.AccountReadDto;
import su.itpro.tasktracker.model.dto.RegisterDto;
import su.itpro.tasktracker.model.enums.Role;
import su.itpro.tasktracker.service.AccountService;
import su.itpro.tasktracker.service.AdminService;

@Controller
@RequestMapping("/admin/account")
@RequiredArgsConstructor
public class AdminAccountController {

  private static final int NAV_PAGE_ITEMS = 5;

  private final AdminService adminService;
  private final AccountService accountService;

  @GetMapping("/find")
  public String myPage(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(defaultValue = "10") int size,
                       AccountFilterFormDto formDto,
                       Model model) {
    var sort = Sort.by("profile_lastname", "profile_firstname", "profile_surname");
    var pageable = PageRequest.of(page - 1, size, sort);
    Page<AccountReadDto> pageAccounts = adminService.findAllAccounts(formDto, pageable);
    model.addAttribute("filter", formDto);
    model.addAttribute("roles", Role.values());
    model.addAttribute("accounts", pageAccounts);
    model.addAttribute("currentPage", pageAccounts.getNumber() + 1);
    model.addAttribute("totalPages", pageAccounts.getTotalPages());
    model.addAttribute("totalItems", pageAccounts.getTotalElements());
    model.addAttribute("pageSize", size);
    model.addAttribute("navPages", NAV_PAGE_ITEMS);
    model.addAttribute("register", RegisterDto.builder().build());
    model.addAttribute("tab", "find");
    return "admin/account-find";
  }

  @GetMapping("/create")
  public String getCreatePage(Model model) {
    model.addAttribute("register", RegisterDto.builder().build());
    model.addAttribute("roles", Role.values());
    model.addAttribute("tab", "create");
    return "admin/account-create";
  }

  @PostMapping("/create")
  public String createNewAccount(@Validated @ModelAttribute("register") RegisterDto registerDto,
                                 BindingResult bindingResult,
                                 Model model) {
    if (!accountService.register(registerDto, bindingResult, Locale.getDefault())) {
      model.addAttribute("tab", "create");
      return "admin/account-create";
    }
    return "redirect:/admin/account/find";
  }

  @PostMapping("/block")
  public String blockAccount(@Validated @ModelAttribute AccountBlockDto blockDto,
                             Principal principal) {
    AccountReadDto account = accountService.findAccountByUsername(principal.getName());
    if (!Objects.equals(account.id(), blockDto.id())) {
      adminService.blockAccount(blockDto);
    } else {
      throw new IllegalArgumentException("You can't block yourself");
    }
    return "redirect:/admin/account/find";
  }

}
