package su.itpro.tasktracker.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import su.itpro.tasktracker.model.entity.ProjectGroup;

public interface ProjectGroupRepository extends JpaRepository<ProjectGroup, Integer> {

  List<ProjectGroup> findAllByProject_Id(Integer projectId);

  List<ProjectGroup> findAllByGroup_IdIn(List<Integer> groupIds);

}
