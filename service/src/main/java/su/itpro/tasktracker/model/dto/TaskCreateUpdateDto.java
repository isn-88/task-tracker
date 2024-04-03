package su.itpro.tasktracker.model.dto;

import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Builder
public record TaskCreateUpdateDto(Long parentId,
                                  Integer projectId,
                                  String type,
                                  String title,
                                  String status,
                                  String priority,
                                  String assignedForm,
                                  Short categoryId,
                                  Long closeAt,
                                  String description) {

}

