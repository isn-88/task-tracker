package su.itpro.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Type {

  FEATURE("Feature"),
  BUGFIX("Bugfix"),
  REFACTORING("Refactoring"),
  RESEARCH("Research"),
  SUPPORT("Support"),
  INSTALLATION("Installation"),
  DOCUMENTATION("Documentation");

  private final String type;

}
