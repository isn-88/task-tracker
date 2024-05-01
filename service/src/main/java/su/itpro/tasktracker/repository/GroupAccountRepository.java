package su.itpro.tasktracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.GroupAccount;

public interface GroupAccountRepository extends JpaRepository<GroupAccount, Long> {

  void deleteAllByGroup(Group group);

  List<GroupAccount> findAllByGroup(Group group);

  void deleteByAccount_IdAndGroup_Id(Long accountId, Integer groupId);

  List<GroupAccount> findAllByAccount_Id(Long accountId);

}
