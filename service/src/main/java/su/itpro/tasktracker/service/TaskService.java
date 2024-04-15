package su.itpro.tasktracker.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

  private final TaskUpdateMapper taskCreateMapper;
  private final TaskReadMapper taskReadMapper;
  private final TaskRepository taskRepository;

  @Transactional(readOnly = true)
  public List<TaskReadDto> findAllBy(TaskFilter filter) {
    return taskRepository.findAllByFilter(filter).stream()
        .map(taskReadMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public Optional<TaskReadDto> findById(Long id) {
    return taskRepository.findById(id)
        .map(taskReadMapper::map);
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
