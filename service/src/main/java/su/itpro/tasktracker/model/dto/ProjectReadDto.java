package su.itpro.tasktracker.model.dto;

import lombok.Builder;

@Builder
public record ProjectReadDto(Integer id,
                             String name,
                             String description) {

}
