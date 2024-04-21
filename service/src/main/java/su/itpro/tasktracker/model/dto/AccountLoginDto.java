package su.itpro.tasktracker.model.dto;

import lombok.Builder;

@Builder
public record AccountLoginDto(String username,
                              String email,
                              String password) {

}
