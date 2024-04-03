package su.itpro.tasktracker.model.mapper;

import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.enums.AssignedType;
import su.itpro.tasktracker.model.util.ProfileUtil;

@Component
public class AssignedAccountMapper implements Mapper<Account, TaskAssignedDto> {

  @Override
  public TaskAssignedDto map(Account account) {
    return TaskAssignedDto.builder()
        .id(account.getId())
        .name(ProfileUtil.getUserFullName(account.getProfile()))
        .type(AssignedType.ACCOUNT)
        .build();
  }
}
