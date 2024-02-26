package su.itpro.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskPriority {
  LOW("Low"),
  NORMAL("Normal"),
  HIGH("High"),
  URGENT("Urgent");

  private final String priority;

}
