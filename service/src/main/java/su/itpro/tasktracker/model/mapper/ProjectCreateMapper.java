package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.ProjectCreateDto;
import su.itpro.tasktracker.model.entity.Project;

@Component
public class ProjectCreateMapper implements Mapper<ProjectCreateDto, Project> {

  @Override
  public Project map(ProjectCreateDto createDto) {
    return Project.builder()
        .name(createDto.name())
        .description(createDto.description())
        .build();
  }
}
