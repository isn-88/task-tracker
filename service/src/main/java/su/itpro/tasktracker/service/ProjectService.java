package su.itpro.tasktracker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dto.ProjectReadDto;
import su.itpro.tasktracker.model.mapper.ProjectReadMapper;
import su.itpro.tasktracker.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectReadMapper projectReadMapper;

  @Transactional(readOnly = true)
  public List<ProjectReadDto> findAll() {
    return projectRepository.findAll().stream()
        .map(projectReadMapper::map)
        .toList();
  }

}
