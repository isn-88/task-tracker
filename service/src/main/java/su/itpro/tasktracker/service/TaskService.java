package su.itpro.tasktracker.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dto.AccountWithGroupsDto;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.model.dto.TaskFilter;
import su.itpro.tasktracker.model.dto.TaskReadDto;
import su.itpro.tasktracker.model.dto.TaskUpdateDto;
import su.itpro.tasktracker.model.mapper.TaskReadMapper;
import su.itpro.tasktracker.model.mapper.TaskUpdateMapper;
import su.itpro.tasktracker.repository.TaskRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

  private static final Integer EMPTY_GROUP = 0;

  private final TaskUpdateMapper taskCreateMapper;
  private final TaskReadMapper taskReadMapper;
  private final TaskRepository taskRepository;

  @Transactional(readOnly = true)
  public Optional<TaskReadDto> findById(Long id) {
    return taskRepository.findById(id)
        .map(taskReadMapper::map);
  }

  @Transactional(readOnly = true)
  public List<TaskReadDto> findAllBy(TaskFilter filter) {
    return taskRepository.findAllByFilter(filter).stream()
        .map(taskReadMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<TaskReadDto> findAllAssignedTask(AccountWithGroupsDto accountWithGroups) {
    TaskFilter filterByAccount = TaskFilter.builder()
        .assignedAccountId(List.of(accountWithGroups.account().id()))
        .build();
    List<Integer> groupIds = accountWithGroups.groups().stream()
        .map(GroupReadDto::id)
        .toList();
    TaskFilter filterByGroup = TaskFilter.builder()
        .assignedGroupId((groupIds.isEmpty()) ? List.of(EMPTY_GROUP) : groupIds)
        .build();
    return Stream.concat(
            taskRepository.findAllByFilter(filterByAccount).stream().map(taskReadMapper::map),
            taskRepository.findAllByFilter(filterByGroup).stream().map(taskReadMapper::map)
        ).toList();
  }

  public TaskReadDto create(TaskUpdateDto taskCreateDto) {
    return Optional.of(taskCreateDto)
        .map(taskCreateMapper::map)
        .map(taskRepository::saveAndFlush)
        .map(taskReadMapper::map)
        .orElseThrow();
  }

  public Optional<TaskReadDto> update(Long id, TaskUpdateDto taskUpdateDto) {
    return taskRepository.findById(id)
        .map(task -> taskCreateMapper.map(taskUpdateDto, task))
        .map(taskRepository::saveAndFlush)
        .map(taskReadMapper::map);
  }

}
