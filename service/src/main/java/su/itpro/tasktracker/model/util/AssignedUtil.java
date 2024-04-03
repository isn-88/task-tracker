package su.itpro.tasktracker.model.util;

import lombok.experimental.UtilityClass;
import su.itpro.tasktracker.model.enums.AssignedType;

@UtilityClass
public class AssignedUtil {

  private static final String NONE = "none";
  private static final String SEPARATOR = ":";

  public Long getAccountId(String assignedValue) {
    if (assignedValue == null || assignedValue.equals(NONE)) {
      return null;
    }
    if (assignedValue.startsWith(AssignedType.ACCOUNT.name())) {
      return Long.parseLong(assignedValue.substring(assignedValue.indexOf(SEPARATOR) + 1));
    }
    return null;
  }

  public Integer getGroupId(String assignedValue) {
    if (assignedValue == null || assignedValue.equals(NONE)) {
      return null;
    }
    if (assignedValue.startsWith(AssignedType.GROUP.name())) {
      return Integer.parseInt(assignedValue.substring(assignedValue.indexOf(SEPARATOR) + 1));
    }
    return null;
  }
}
