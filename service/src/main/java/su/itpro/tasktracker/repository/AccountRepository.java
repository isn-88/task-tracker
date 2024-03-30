package su.itpro.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, AccountFilterRepository {

}
