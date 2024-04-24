package su.itpro.tasktracker.repository.impl;

import static su.itpro.tasktracker.model.entity.QAccount.account;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import su.itpro.tasktracker.model.dao.QPredicate;
import su.itpro.tasktracker.model.dto.AccountLoginDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.repository.AccountFilterRepository;

@RequiredArgsConstructor
public class AccountFilterRepositoryImpl implements AccountFilterRepository {

  private final EntityManager entityManager;

  public Optional<Account> findByFilter(AccountLoginDto loginDto) {
    Predicate predicate = QPredicate.builder()
        .add(loginDto.username(), account.username::eq)
        .add(loginDto.email(), account.email::eq)
        .buildOr();

    return Optional.ofNullable(
        new JPAQuery<Account>(entityManager)
            .select(account)
            .from(account)
            .where(predicate)
            .fetchOne()
    );
  }

}
