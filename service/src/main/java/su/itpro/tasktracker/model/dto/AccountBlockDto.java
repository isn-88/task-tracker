package su.itpro.tasktracker.model.dto;

import jakarta.validation.constraints.NotNull;

public record AccountBlockDto(@NotNull Long id,
                              @NotNull Boolean isEnabled) {

}
