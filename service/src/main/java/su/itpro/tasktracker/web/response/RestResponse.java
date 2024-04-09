package su.itpro.tasktracker.web.response;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;


public record RestResponse<T>(Boolean success,

                              @JsonInclude(NON_NULL)
                              String message,

                              @JsonInclude(NON_NULL)
                              T data) {

  public static <T> RestResponse<T> success(T data) {
    return new RestResponse<>(true, null, data);
  }

  public static <T> RestResponse<T> error(String message) {
    return new RestResponse<>(false, message, null);
  }
}
