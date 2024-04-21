package su.itpro.tasktracker.service;

import static su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields.currentPassword;
import static su.itpro.tasktracker.model.dto.PasswordUpdateDto.Fields.repeatPassword;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import su.itpro.tasktracker.exception.PasswordMismatchException;
import su.itpro.tasktracker.model.dto.AccountReadDto;
import su.itpro.tasktracker.model.dto.AccountUpdateDto;
import su.itpro.tasktracker.model.dto.AccountWithGroupsDto;
import su.itpro.tasktracker.model.dto.PasswordUpdateDto;
import su.itpro.tasktracker.model.dto.ProfileUpdateDto;
import su.itpro.tasktracker.model.dto.RegisterDto;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.mapper.AccountReadMapper;
import su.itpro.tasktracker.model.mapper.AccountRegistrationMapper;
import su.itpro.tasktracker.model.mapper.AccountWithGroupMapper;
import su.itpro.tasktracker.model.mapper.AssignedAccountMapper;
import su.itpro.tasktracker.model.mapper.AssignedGroupMapper;
import su.itpro.tasktracker.model.mapper.ProfileRegistrationMapper;
import su.itpro.tasktracker.model.mapper.UserDetailsMapper;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.GroupRepository;
import su.itpro.tasktracker.validation.RegisterPostValidator;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

  private final AccountRepository accountRepository;
  private final GroupRepository groupRepository;
  private final AccountReadMapper accountReadMapper;
  private final AccountWithGroupMapper accountWithGroupMapper;
  private final AccountRegistrationMapper accountRegistrationMapper;
  private final AssignedAccountMapper assignedAccountMapper;
  private final AssignedGroupMapper assignedGroupMapper;
  private final UserDetailsMapper userDetailsMapper;
  private final ProfileRegistrationMapper profileRegistrationMapper;
  private final PasswordEncoder passwordEncoder;
  private final RegisterPostValidator registerPostValidator;

  @Transactional(readOnly = true)
  public List<TaskAssignedDto> getAllAssigned() {
    return Stream.concat(
            groupRepository.findAll().stream().map(assignedGroupMapper::map),
            accountRepository.findAll().stream().map(assignedAccountMapper::map)
        )
        .toList();
  }

  @Transactional(readOnly = true)
  public AccountReadDto findAccountByUsername(String username) {
    return accountRepository.findByUsername(username)
        .map(accountReadMapper::map)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
  }

  @Transactional(readOnly = true)
  public AccountWithGroupsDto findAccountWithGroups(String username) {
    return accountRepository.findByUsername(username)
        .map(accountWithGroupMapper::map)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
  }

  public void register(RegisterDto dto, BindingResult bindingResult, Locale locale) {
    registerPostValidator.validate(dto, bindingResult, locale);
    if (bindingResult.hasErrors()) {
      return;
    }
    Account newAccount = accountRegistrationMapper.map(dto);
    accountRepository.save(newAccount);
    Profile profile = profileRegistrationMapper.map(dto);
    profile.setAccount(newAccount);
  }

  public void update(AccountUpdateDto updateDto, String username) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
    account.setEmail(updateDto.email());
    account.setUsername(updateDto.username());
    accountRepository.saveAndFlush(account);
  }

  public void updateProfile(ProfileUpdateDto updateDto, String username) {
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
    Profile profile = account.getProfile();
    profile.setLastname(updateDto.lastname());
    profile.setFirstname(updateDto.firstname());
    profile.setSurname(updateDto.surname());
    profile.setGender(updateDto.gender());
    profile.setAboutMe(updateDto.aboutMe());
  }

  public void updatePassword(PasswordUpdateDto updateDto, String username) {
    if (!Objects.equals(updateDto.newPassword(), updateDto.repeatPassword())) {
      throw new PasswordMismatchException(
          "New password and repeat password has mismatch",
          repeatPassword
      );
    }
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
    if (!passwordEncoder.matches(updateDto.currentPassword(), account.getPassword())) {
      throw new PasswordMismatchException(
          "Input current password is incorrect",
          currentPassword
      );
    }
    account.setPassword(passwordEncoder.encode(updateDto.newPassword()));
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username.contains("@")) {
      return accountRepository.findByEmail(username)
          .map(userDetailsMapper::map)
          .orElseThrow(() -> new UsernameNotFoundException(
              "Account with email: " + username + " not found"));
    }
    return accountRepository.findByUsername(username)
        .map(userDetailsMapper::map)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
  }

}
