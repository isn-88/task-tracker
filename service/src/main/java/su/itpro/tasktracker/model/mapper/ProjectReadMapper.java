package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.ProjectReadDto;
import su.itpro.tasktracker.model.entity.Project;

@Component
public class ProjectReadMapper implements Mapper<Project, ProjectReadDto> {

  @Override
  public ProjectReadDto map(Project project) {
    return ProjectReadDto.builder()
        .id(project.getId())
        .name(project.getName())
        .description(project.getDescription())
        .build();
  }
}
