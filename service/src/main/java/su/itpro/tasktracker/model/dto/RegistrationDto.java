package su.itpro.tasktracker.model.dto;

import su.itpro.tasktracker.model.enums.Role;

public record RegistrationDto(String email,
                              String username,
                              String password,
                              Role role,
                              String lastname,
                              String firstname) {

}
