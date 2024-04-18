package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Builder
public record TaskUpdateDto(Long parentId,

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

                            LocalDate startDate,

                            LocalDate endDate,

                            Long closeAt,

                            String description) {

}

