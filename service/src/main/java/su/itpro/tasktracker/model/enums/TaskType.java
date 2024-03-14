package su.itpro.tasktracker.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskType {

  FEATURE("Feature"),
  BUGFIX("Bugfix"),
  REFACTORING("Refactoring"),
  RESEARCH("Research"),
  SUPPORT("Support"),
  INSTALLATION("Installation"),
  DOCUMENTATION("Documentation");

  private final String type;

}
