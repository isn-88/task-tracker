package su.itpro.tasktracker.model.dto;

import lombok.Builder;
import su.itpro.tasktracker.model.enums.TaskType;

@Builder
public record TaskParentDto(Long id,
                            TaskType type,
                            String title) {

}
