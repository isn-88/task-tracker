package su.itpro.model.dto;

import java.util.List;
import lombok.Builder;
import su.itpro.model.enums.TaskPriority;
import su.itpro.model.enums.TaskStatus;
import su.itpro.model.enums.TaskType;

@Builder
public record TaskFilter(Long parentId,
                         List<TaskType> types,
                         List<TaskStatus> statuses,
                         List<TaskPriority> priorities) {

}
