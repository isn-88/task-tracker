package su.itpro.model.enums;

import lombok.Getter;

@Getter
public enum Status {
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

  Status(String status) {
    this.status = status;
  }

}
