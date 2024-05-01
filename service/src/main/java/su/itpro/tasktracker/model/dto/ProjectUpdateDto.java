package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectUpdateDto(Integer id,

                               @NotBlank
                               String name,

                               @NotBlank
                               String description) {

}
