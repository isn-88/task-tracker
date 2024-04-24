package su.itpro.tasktracker.model.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record AccountWithGroupsDto(AccountReadDto account,
                                   List<GroupReadDto> groups) {

}
