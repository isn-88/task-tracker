package su.itpro.tasktracker.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(basePackages = "su.itpro.tasktracker.web.controller")
public class ControllerExceptionHandler {

  @ExceptionHandler(Exception.class)
  public String handleExceptions(Exception exception) {
    log.error("Failed to return response", exception);
    return "error/errorPage";
  }

}
