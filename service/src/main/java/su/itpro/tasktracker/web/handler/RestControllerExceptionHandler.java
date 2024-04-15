package su.itpro.tasktracker.web.handler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import su.itpro.tasktracker.exception.ResourceNotFoundException;
import su.itpro.tasktracker.web.response.RestResponse;

@Slf4j
@RestControllerAdvice(basePackages = "su.itpro.tasktracker.web.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @Nonnull MethodArgumentNotValidException ex,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode status,
      @Nonnull WebRequest request) {
    RestResponse<?> response = RestResponse.error("Validation field error");
    return new ResponseEntity<>(response, status);
  }

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException.class)
  public RestResponse<?> handleResourceNotFound(ResourceNotFoundException ex) {
    log.error("Resource not found:", ex);
    return RestResponse.error(ex.getMessage());
  }

  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ExceptionHandler(Exception.class)
  public RestResponse<?> handleExceptions(Exception ex) {
    log.error("Failed to return response", ex);
    return RestResponse.error("Internal server error");
  }

}
