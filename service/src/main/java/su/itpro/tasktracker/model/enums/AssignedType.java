package su.itpro.tasktracker.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AssignedType {
  ACCOUNT("accounts"),
  GROUP("groups");

  private final String path;

}
