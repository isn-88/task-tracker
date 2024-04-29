package su.itpro.tasktracker.service;

import static su.itpro.tasktracker.model.entity.QAccount.account;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dao.QPredicate;
import su.itpro.tasktracker.model.dto.AccountBlockDto;
import su.itpro.tasktracker.model.dto.AccountFilterDto;
import su.itpro.tasktracker.model.dto.AccountFilterFormDto;
import su.itpro.tasktracker.model.dto.AccountReadDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.mapper.AccountFilterMapper;
import su.itpro.tasktracker.model.mapper.AccountReadMapper;
import su.itpro.tasktracker.repository.AccountRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

  private final AccountRepository accountRepository;
  private final AccountReadMapper accountReadMapper;
  private final AccountFilterMapper accountFilterMapper;

  @Transactional(readOnly = true)
  public Page<AccountReadDto> findAllAccounts(AccountFilterFormDto formDto, PageRequest pageable) {
    AccountFilterDto filterDto = accountFilterMapper.map(formDto);
    Predicate accountPredicate = QPredicate.builder()
        .add(filterDto.email(), account.email::likeIgnoreCase)
        .add(filterDto.username(), account.username::likeIgnoreCase)
        .add(filterDto.role(), account.role::eq)
        .add(filterDto.isEnabled(), account.enabled::eq)
        .buildAnd();
    Predicate profilePredicate = QPredicate.builder()
        .add(filterDto.fullNameParts(), account.profile.firstname.toLowerCase()::in)
        .add(filterDto.fullNameParts(), account.profile.lastname.toLowerCase()::in)
        .add(filterDto.fullNameParts(), account.profile.surname.toLowerCase()::in)
        .buildOr();
    Predicate predicate = ExpressionUtils.allOf(accountPredicate, profilePredicate);
    if (predicate == null) {
      return accountRepository.findAll(pageable).map(accountReadMapper::map);
    }
    return accountRepository.findAll(predicate, pageable).map(accountReadMapper::map);
  }

  public void blockAccount(AccountBlockDto blockDto) {
    Account account = accountRepository.findById(blockDto.id())
        .orElseThrow(() -> new IllegalArgumentException(
            "Account with id: " + blockDto.id() + " not found"));
    account.setEnabled(blockDto.isEnabled());
    accountRepository.saveAndFlush(account);
  }
}
