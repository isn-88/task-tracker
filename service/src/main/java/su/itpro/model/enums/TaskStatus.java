package su.itpro.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TaskStatus {
  NEW("New"),
  ASSIGNED("Assigned"),
  IN_PROGRESS("In Progress"),
  RESOLVED("Resolved"),
  CODE_REVIEW("Code Review"),
  TESTING("Testing"),
  DELAYED("Delayed"),
  REJECTED("Rejected"),
  CLOSED("Closed");

  private final String status;

}
