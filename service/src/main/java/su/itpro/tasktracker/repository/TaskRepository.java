package su.itpro.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>,
    TaskFilterRepository, RevisionRepository<Task, Long, Integer> {

}
