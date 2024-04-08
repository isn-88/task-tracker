package su.itpro.tasktracker.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice(basePackages = "su.itpro.tasktracker.web.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleExceptions(Exception exception) {
    log.error("Failed to return response", exception);
    return ResponseEntity.internalServerError().build();
  }

}
