package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Builder
public record TaskCreateUpdateDto(Long parentId,
                                  @NotNull
                                  Integer projectId,
                                  @NotBlank
                                  String type,
                                  @NotBlank
                                  String title,
                                  @NotBlank
                                  String status,
                                  @NotBlank
                                  String priority,
                                  String assignedForm,
                                  Short categoryId,
                                  Long closeAt,
                                  String description) {

}

