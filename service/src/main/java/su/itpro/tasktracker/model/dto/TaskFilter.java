package su.itpro.tasktracker.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;
import su.itpro.tasktracker.model.enums.TaskPriority;
import su.itpro.tasktracker.model.enums.TaskStatus;
import su.itpro.tasktracker.model.enums.TaskType;

@Builder
@FieldNameConstants
public record TaskFilter(String findPattern,
                         Long parentId,
                         Long ownerId,
                         List<Long> assignedAccountId,
                         List<Integer> assignedGroupId,
                         List<TaskType> types,
                         List<TaskStatus> statuses,
                         List<TaskPriority> priorities) {

}
