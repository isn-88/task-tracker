package su.itpro.tasktracker.model.mapper;

import static java.util.function.Predicate.not;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import su.itpro.tasktracker.model.dto.AccountFilterDto;
import su.itpro.tasktracker.model.dto.AccountFilterFormDto;
import su.itpro.tasktracker.model.enums.Role;

@Component
public class AccountFilterMapper implements Mapper<AccountFilterFormDto, AccountFilterDto> {

  private static final String DELIMITER = " ";

  @Override
  public AccountFilterDto map(AccountFilterFormDto formDto) {
    return AccountFilterDto.builder()
        .id(formDto.id())
        .username(notEmpty(formDto.username()) ? getLikePattern(formDto.username()) : null)
        .email(notEmpty(formDto.email()) ? getLikePattern(formDto.email()) : null)
        .role(notEmpty(formDto.role()) ? Role.valueOf(formDto.role()) : null)
        .isEnabled(notEmpty(formDto.status()) ? Boolean.valueOf(formDto.status()) : null)
        .fullNameParts(notEmpty(formDto.fullName()) ? getNameParts(formDto.fullName()) : null)
        .build();
  }

  private boolean notEmpty(String value) {
    return value != null && !value.isBlank() && !value.equals("null");
  }

  private String getLikePattern(String value) {
    return "%" + value + "%";
  }

  private List<String> getNameParts(String value) {
    String[] split = value.split(DELIMITER);
    return Arrays.stream(split)
        .map(String::strip)
        .filter(not(String::isBlank))
        .map(String::toLowerCase)
        .toList();
  }

}
