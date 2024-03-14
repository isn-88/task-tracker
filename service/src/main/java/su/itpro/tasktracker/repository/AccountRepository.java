package su.itpro.tasktracker.repository;

import static su.itpro.tasktracker.model.entity.QAccount.account;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.dao.QPredicate;
import su.itpro.tasktracker.model.dto.AccountLoginDto;
import su.itpro.tasktracker.model.entity.Account;

@Repository
public class AccountRepository extends BaseRepository<UUID, Account> {

  public AccountRepository(EntityManager entityManager) {
    super(entityManager, Account.class);
  }

  public Optional<Account> findByLoginOrEmail(AccountLoginDto loginDto) {
    Predicate predicate = QPredicate.builder()
        .add(loginDto.login(), account.login::eq)
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