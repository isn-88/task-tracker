package su.itpro.tasktracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

  @Query(value = "SELECT gr.id, gr.name, gr.created_at, count(ga.account_id) " +
                 "FROM groups gr " +
                 "LEFT JOIN groups_account ga " +
                 "ON gr.id = ga.group_id " +
                 "GROUP BY gr.id, gr.name " +
                 "ORDER BY gr.name",
      nativeQuery = true)
  List<Object[]> countParticipant();

}
