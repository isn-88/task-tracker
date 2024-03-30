package su.itpro.tasktracker.model.util;

import lombok.experimental.UtilityClass;
import su.itpro.tasktracker.model.enums.AssignedType;

@UtilityClass
public class AssignedUtil {

  public Long getAccountId(String assignedValue) {
    if (assignedValue == null || "none".equals(assignedValue)) {
      return null;
    }
    if (AssignedType.A.name().equals(assignedValue.substring(0, 1))) {
      return Long.parseLong(assignedValue.substring(1));
    }
    return null;
  }

  public Integer getGroupId(String assignedValue) {
    if (assignedValue == null || "none".equals(assignedValue)) {
      return null;
    }
    if (AssignedType.G.name().equals(assignedValue.substring(0, 1))) {
      return Integer.parseInt(assignedValue.substring(1));
    }
    return null;
  }
}
