package su.itpro.tasktracker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.itpro.tasktracker.exception.ResourceNotFoundException;
import su.itpro.tasktracker.model.dto.AccountAboutDto;
import su.itpro.tasktracker.model.dto.GroupReadDto;
import su.itpro.tasktracker.model.dto.GroupUpdateDto;
import su.itpro.tasktracker.model.entity.Account;
import su.itpro.tasktracker.model.entity.Group;
import su.itpro.tasktracker.model.entity.GroupAccount;
import su.itpro.tasktracker.model.mapper.AccountAboutMapper;
import su.itpro.tasktracker.model.mapper.GroupCountMapper;
import su.itpro.tasktracker.model.mapper.GroupCreateMapper;
import su.itpro.tasktracker.model.mapper.GroupReadMapper;
import su.itpro.tasktracker.repository.AccountRepository;
import su.itpro.tasktracker.repository.GroupAccountRepository;
import su.itpro.tasktracker.repository.GroupRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupService {

  private final AccountRepository accountRepository;
  private final GroupRepository groupRepository;
  private final GroupAccountRepository groupAccountRepository;
  private final AccountAboutMapper accountAboutMapper;
  private final GroupReadMapper groupReadMapper;
  private final GroupCreateMapper groupCreateMapper;
  private final GroupCountMapper groupCountMapper;

  @Transactional(readOnly = true)
  public GroupReadDto findById(Integer groupId) {
    return groupRepository.findById(groupId)
        .map(groupReadMapper::map)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Group with id: " + groupId + " not found"));
  }

  @Transactional(readOnly = true)
  public List<GroupReadDto> findAll() {
    return groupRepository.findAll(Sort.by("name")).stream()
        .map(groupReadMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<GroupReadDto> findAllGroupsWithCount() {
    return groupRepository.countParticipant().stream()
        .map(groupCountMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<AccountAboutDto> findAccountsByGroupId(Integer groupId) {
    Group group = groupRepository.findById(groupId)
        .orElseThrow(() -> new ResourceNotFoundException(
            "Group with id: " + groupId + " not found"));
    return groupAccountRepository.findAllByGroup(group).stream()
        .map(GroupAccount::getAccount)
        .map(accountAboutMapper::map)
        .toList();
  }

  @Transactional(readOnly = true)
  public Set<Integer> findAllGroupIdsByAccountId(Long accountId) {
    return groupAccountRepository.findAllByAccount_Id(accountId).stream()
        .map(ga -> ga.getGroup().getId())
        .collect(Collectors.toSet());
  }

  public void createGroup(GroupUpdateDto createDto) {
    Group group = groupCreateMapper.map(createDto);
    groupRepository.saveAndFlush(group);
  }

  public void updateGroup(GroupUpdateDto updateDto) {
    Group group = groupRepository.findById(updateDto.id())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Group with id: " + updateDto.id() + " not found"));
    group.setName(updateDto.name());
    groupRepository.saveAndFlush(group);
  }

  public void deleteGroup(GroupUpdateDto updateDto) {
    Group group = groupRepository.findById(updateDto.id())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Group with id: " + updateDto.id() + " not found"));
    groupAccountRepository.deleteAllByGroup(group);
    groupRepository.delete(group);
  }

  public void exitFromGroup(Long accountId, Integer groupId) {
    groupAccountRepository.deleteByAccount_IdAndGroup_Id(accountId, groupId);
  }

  public void updateAccountGroups(Long accountId, List<Integer> groupIds) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new IllegalArgumentException(
            "Account with id: " + accountId + " not found"));
    List<GroupAccount> groupAccounts = account.getGroupAccounts();
    for (GroupAccount groupAccount : groupAccounts) {
      if (!groupIds.contains(groupAccount.getGroup().getId())) {
        groupAccountRepository.delete(groupAccount);
      }
    }
    ArrayList<Integer> listGroupIds = new ArrayList<>(groupIds);
    listGroupIds.removeAll(groupAccounts.stream().map(ga -> ga.getGroup().getId()).toList());
    List<GroupAccount> newGroupAccounts = listGroupIds.stream()
        .map(id -> groupRepository.findById(id).orElseThrow())
        .map(group -> createAndSaveGroupAccount(group, account))
        .toList();
    groupAccounts.addAll(newGroupAccounts);
    accountRepository.save(account);
  }

  private GroupAccount createAndSaveGroupAccount(Group group, Account account) {
    GroupAccount groupAccount = new GroupAccount();
    groupAccount.setGroup(group);
    groupAccount.setAccount(account);
    return groupAccountRepository.save(groupAccount);
  }
}
