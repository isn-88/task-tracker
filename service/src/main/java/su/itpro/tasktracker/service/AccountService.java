package su.itpro.tasktracker.service;

import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.exception.IllegalPasswordException;
import su.itpro.tasktracker.model.dto.AccountReadDto;
import su.itpro.tasktracker.model.dto.AccountUpdateDto;
import su.itpro.tasktracker.model.dto.PasswordUpdateDto;
import su.itpro.tasktracker.model.dto.ProfileUpdateDto;
import su.itpro.tasktracker.model.dto.RegistrationDto;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Profile;
import su.itpro.tasktracker.model.mapper.AccountReadMapper;
import su.itpro.tasktracker.model.mapper.AccountRegistrationMapper;
import su.itpro.tasktracker.model.mapper.AssignedAccountMapper;
import su.itpro.tasktracker.model.mapper.AssignedGroupMapper;
import su.itpro.tasktracker.model.mapper.ProfileRegistrationMapper;
import su.itpro.tasktracker.model.mapper.UserDetailsMapper;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService implements UserDetailsService {

  private final AccountRepository accountRepository;
  private final GroupRepository groupRepository;
  private final AccountReadMapper accountReadMapper;
  private final AccountRegistrationMapper accountRegistrationMapper;
  private final AssignedAccountMapper assignedAccountMapper;
  private final AssignedGroupMapper assignedGroupMapper;
  private final UserDetailsMapper userDetailsMapper;
  private final ProfileRegistrationMapper profileRegistrationMapper;
  private final PasswordEncoder passwordEncoder;

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

  public void registration(RegistrationDto dto) {
    // TODO validation dto
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
    // TODO custom password fields validator
    Account account = accountRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "Account with username: " + username + " not found"));
    if (!passwordEncoder.matches(updateDto.currentPassword(), account.getPassword())) {
      throw new IllegalPasswordException("Input current password is incorrect");
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
