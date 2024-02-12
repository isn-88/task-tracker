package su.itpro.model.enums;

import lombok.Getter;

@Getter
public enum Priority {
  LOW("Low"),
  NORMAL("Normal"),
  HIGH("High"),
  URGENT("Urgent");

  private final String priority;

  Priority(String priority) {
    this.priority = priority;
  }
}
