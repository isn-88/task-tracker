package su.itpro.tasktracker.model.dto;

import java.time.Instant;
import java.time.LocalDate;
import lombok.Builder;
import su.itpro.tasktracker.model.entity.Category;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;

@Builder
public record TaskReadDto(Long id,
                          TaskParentDto parentTask,
                          ProjectReadDto project,
                          TaskType type,
                          String title,
                          TaskStatus status,
                          TaskPriority priority,
                          TaskAssignedDto assigned,
                          Category category,
                          LocalDate startDate,
                          LocalDate endDate,
                          Instant createAt,
                          Instant closeAt,
                          Short progress,
                          String description) {

}
