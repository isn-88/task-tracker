package su.itpro.tasktracker.model.dto;

import lombok.Builder;

@Builder
public record AccountLoginDto(String login,
                              String email,
                              String password) {

}
