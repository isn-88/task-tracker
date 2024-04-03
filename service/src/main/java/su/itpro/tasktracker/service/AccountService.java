package su.itpro.tasktracker.service;

import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dto.TaskAssignedDto;
import su.itpro.tasktracker.model.mapper.AssignedAccountMapper;
import su.itpro.tasktracker.model.mapper.AssignedGroupMapper;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

  private final AccountRepository accountRepository;
  private final GroupRepository groupRepository;
  private final AssignedAccountMapper assignedAccountMapper;
  private final AssignedGroupMapper assignedGroupMapper;

  public List<TaskAssignedDto> getAllAssigned() {
    return Stream.concat(
            groupRepository.findAll().stream().map(assignedGroupMapper::map),
            accountRepository.findAll().stream().map(assignedAccountMapper::map)
        )
        .toList();
  }

}
