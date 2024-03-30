package su.itpro.tasktracker.model.dto;

import lombok.Builder;
import su.itpro.tasktracker.model.enums.AssignedType;

@Builder
public record TaskAssignedDto(Number id,
                              String name,
                              AssignedType type) {

}
