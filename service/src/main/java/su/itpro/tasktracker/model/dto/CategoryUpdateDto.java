package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Builder
public record CategoryUpdateDto(@NotBlank
                                String name) {

}
