package su.itpro.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Priority {
  LOW("Low"),
  NORMAL("Normal"),
  HIGH("High"),
  URGENT("Urgent");

  private final String priority;

}
