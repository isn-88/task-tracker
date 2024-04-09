package su.itpro.tasktracker.model.dto;

import lombok.Builder;

@Builder
public record CategoryReadDto(Short id,
                              String name) {

}
