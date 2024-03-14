package su.itpro.tasktracker.model.dto;

import java.util.List;
import java.util.UUID;
import lombok.Builder;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;

@Builder
public record TaskFilter(UUID accountId,
                         Long parentId,
                         List<TaskType> types,
                         List<TaskStatus> statuses,
                         List<TaskPriority> priorities) {

}
