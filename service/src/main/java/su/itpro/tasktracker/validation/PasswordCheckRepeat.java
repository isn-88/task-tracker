package su.itpro.tasktracker.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordCheckRepeatValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordCheckRepeat {

  String message() default "New password and repeat password not match";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}
