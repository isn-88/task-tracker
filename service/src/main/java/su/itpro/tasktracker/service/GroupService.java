package su.itpro.tasktracker.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.model.dto.GroupCreateDto;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.mapper.GroupCreateMapper;
import su.itpro.tasktracker.model.mapper.GroupReadMapper;
import su.itpro.tasktracker.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final GroupRepository groupRepository;
  private final GroupReadMapper groupReadMapper;
  private final GroupCreateMapper groupCreateMapper;

  @Transactional(readOnly = true)
  public List<GroupReadDto> findAll() {
    return groupRepository.findAll().stream()
        .map(groupReadMapper::map)
        .toList();
  }

  public void createGroup(GroupCreateDto createDto) {
    Group group = groupCreateMapper.map(createDto);
    groupRepository.saveAndFlush(group);
  }

}
