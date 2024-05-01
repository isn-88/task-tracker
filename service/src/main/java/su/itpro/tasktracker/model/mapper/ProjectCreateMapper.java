package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.ProjectUpdateDto;
import su.itpro.tasktracker.model.entity.Project;

@Component
public class ProjectCreateMapper implements Mapper<ProjectUpdateDto, Project> {

  @Override
  public Project map(ProjectUpdateDto createDto) {
    return Project.builder()
        .name(createDto.name())
        .description(createDto.description())
        .build();
  }
}
