package su.itpro.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import su.itpro.tasktracker.model.entity.GroupAccount;

public interface GroupAccountRepository extends JpaRepository<GroupAccount, Long> {

}
