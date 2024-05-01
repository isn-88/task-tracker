package su.itpro.tasktracker.model.dto;

public record AccountFilterFormDto(Long id,
                                   String username,
                                   String fullName,
                                   String email,
                                   String role,
                                   String status) {

}
