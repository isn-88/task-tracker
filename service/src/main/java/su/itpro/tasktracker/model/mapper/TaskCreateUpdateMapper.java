package su.itpro.tasktracker.model.mapper;

import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.TaskCreateUpdateDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Category;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.Project;
import su.itpro.tasktracker.model.entity.Task;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;
import su.itpro.tasktracker.model.util.AssignedUtil;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.CategoryRepository;
import su.itpro.tasktracker.repository.GroupRepository;
import su.itpro.tasktracker.repository.ProjectRepository;
import su.itpro.tasktracker.repository.TaskRepository;
import su.itpro.tasktracker.web.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class TaskCreateUpdateMapper implements Mapper<TaskCreateUpdateDto, Task> {

  private final AccountRepository accountRepository;
  private final CategoryRepository categoryRepository;
  private final GroupRepository groupRepository;
  private final ProjectRepository projectRepository;
  private final TaskRepository taskRepository;

  @Override
  public Task map(TaskCreateUpdateDto dto) {
    return copy(dto, new Task());
  }

  @Override
  public Task map(TaskCreateUpdateDto dto, Task task) {
    return copy(dto, task);
  }

  private Task copy(TaskCreateUpdateDto dto, Task task) {
    task.setParent(getParentTask(dto.parentId()));
    task.setProject(getProject(dto.projectId()));
    task.setType(TaskType.valueOf(dto.type()));
    task.setTitle(dto.title());
    task.setStatus(TaskStatus.valueOf(dto.status()));
    task.setPriority(TaskPriority.valueOf(dto.priority()));
    task.setAssignedAccount(getAccount(AssignedUtil.getAccountId(dto.assignedForm())));
    task.setAssignedGroup(getGroup(AssignedUtil.getGroupId(dto.assignedForm())));
    task.setCategory(getCategory(dto.categoryId()));
    task.setCreateAt(Instant.now());
    task.setCloseAt(getCloseAt(dto.closeAt()));
    task.setDescription(dto.description());
    return task;
  }

  private Task getParentTask(Long id) {
    return Optional.ofNullable(id)
        .map(taskId -> taskRepository.findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Parent task with id: " + id + " not found"))
        )
        .orElse(null);
  }

  private Project getProject(Integer id) {
    return Optional.of(id)
        .flatMap(projectRepository::findById)
        .orElseThrow();
  }

  private Account getAccount(Long id) {
    return Optional.ofNullable(id)
        .flatMap(accountRepository::findById)
        .orElse(null);
  }

  private Group getGroup(Integer id) {
    return Optional.ofNullable(id)
        .flatMap(groupRepository::findById)
        .orElse(null);
  }

  private Category getCategory(Short id) {
    return Optional.ofNullable(id)
        .flatMap(categoryRepository::findById)
        .orElse(null);
  }

  private Instant getCloseAt(Long timestamp) {
    return Optional.ofNullable(timestamp)
        .map(Instant::ofEpochMilli)
        .orElse(null);
  }

}
