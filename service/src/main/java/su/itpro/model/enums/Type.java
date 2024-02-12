package su.itpro.model.enums;

public enum Type {

  FEATURE("Feature"),
  BUGFIX("Bugfix"),
  REFACTORING("Refactoring"),
  RESEARCH("Research"),
  SUPPORT("Support"),
  INSTALLATION("Installation"),
  DOCUMENTATION("Documentation");

  private final String type;

  Type(String type) {
    this.type = type;
  }
}
