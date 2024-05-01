package su.itpro.tasktracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import su.itpro.tasktracker.model.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer>,
    QuerydslPredicateExecutor<Project> {

}
