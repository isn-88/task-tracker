package su.itpro.tasktracker.service;

import static java.util.stream.Collectors.toSet;
import static su.itpro.tasktracker.model.entity.QProject.project;

import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.exception.ResourceNotFoundException;
import su.itpro.tasktracker.model.dao.QPredicate;
import su.itpro.tasktracker.model.dto.AccountWithGroupsDto;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.model.dto.ProjectFindDto;
import su.itpro.tasktracker.model.dto.ProjectReadDto;
import su.itpro.tasktracker.model.dto.ProjectUpdateDto;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.ProjectGroup;
import su.itpro.tasktracker.model.mapper.ProjectCreateMapper;
import su.itpro.tasktracker.model.mapper.ProjectReadMapper;
import su.itpro.tasktracker.repository.GroupRepository;
import su.itpro.tasktracker.repository.ProjectGroupRepository;
import su.itpro.tasktracker.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectGroupRepository projectGroupRepository;
  private final GroupRepository groupRepository;
  private final ProjectReadMapper projectReadMapper;
  private final ProjectCreateMapper projectCreateMapper;

  @Transactional(readOnly = true)
  public List<ProjectReadDto> findAll() {
    return projectRepository.findAll().stream()
        .map(projectReadMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public Optional<ProjectReadDto> findById(Integer projectId) {
    return projectRepository.findById(projectId)
        .map(projectReadMapper::map);
  }

  @Transactional(readOnly = true)
  public Set<Integer> findAllGroupIdsByProjectId(Integer projectId) {
    return projectGroupRepository.findAllByProject_Id(projectId).stream()
        .map(pg -> pg.getGroup().getId())
        .collect(toSet());
  }

  @Transactional(readOnly = true)
  public List<ProjectReadDto> findAllAccessedProjects(AccountWithGroupsDto accountWithGroupsDto) {
    List<Integer> accountGroupIds = accountWithGroupsDto.groups().stream()
        .map(GroupReadDto::id)
        .toList();
    return projectGroupRepository.findAllByGroup_IdIn(accountGroupIds).stream()
        .map(ProjectGroup::getProject)
        .map(projectReadMapper::map)
        .toList();
  }

  public void createProject(ProjectUpdateDto createDto) {
    Project project = projectCreateMapper.map(createDto);
    projectRepository.saveAndFlush(project);
  }

  public void updateProject(ProjectUpdateDto updateDto) {
    Project project = projectRepository.findById(updateDto.id())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Project with id: " + updateDto + " not found"));
    project.setName(updateDto.name());
    project.setDescription(updateDto.description());
    projectRepository.saveAndFlush(project);
  }

  public List<ProjectReadDto> findAllProjects(ProjectFindDto findDto, Sort sort) {
    Predicate predicate = QPredicate.builder()
        .add(findDto.findById(), project.id::eq)
        .buildAnd();

    if (predicate == null) {
      return projectRepository.findAll(sort).stream()
          .map(projectReadMapper::map)
          .toList();
    }
    Iterable<Project> projectIterable = projectRepository.findAll(predicate, sort);
    List<ProjectReadDto> projectReadDtoList = new ArrayList<>();
    projectIterable.forEach(project -> projectReadDtoList.add(projectReadMapper.map(project)));
    return projectReadDtoList;
  }

  public void updateProjectGroups(Integer projectId, List<Integer> groupIds) {
    Project project = projectRepository.findById(projectId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Project with id: " + projectId + " not found"));
    List<ProjectGroup> projectGroups = project.getProjectGroup();
    for (ProjectGroup projectGroup : projectGroups) {
      if (!groupIds.contains(projectGroup.getGroup().getId())) {
        projectGroupRepository.delete(projectGroup);
      }
    }
    ArrayList<Integer> listGroupIds = new ArrayList<>(groupIds);
    listGroupIds.removeAll(projectGroups.stream().map(ga -> ga.getGroup().getId()).toList());
    List<ProjectGroup> newProjectGroup = listGroupIds.stream()
        .map(id -> groupRepository.findById(id).orElseThrow())
        .map(group -> createAndSaveGroupProject(group, project))
        .toList();
    projectGroups.addAll(newProjectGroup);
    projectRepository.save(project);
  }

  private ProjectGroup createAndSaveGroupProject(Group group, Project project) {
    ProjectGroup projectGroup = new ProjectGroup();
    projectGroup.setGroup(group);
    projectGroup.setProject(project);
    return projectGroupRepository.save(projectGroup);
  }

}
