package su.itpro.tasktracker.exception;

public class IllegalPasswordException extends RuntimeException {

  public IllegalPasswordException(String message) {
    super(message);
  }
}
