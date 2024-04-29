package su.itpro.tasktracker.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountFilterRepository,
    QuerydslPredicateExecutor<Account> {

  Boolean existsByEmail(String email);

  Boolean existsByUsername(String username);

  Optional<Account> findByUsername(String username);

  Optional<Account> findByEmail(String email);
}
