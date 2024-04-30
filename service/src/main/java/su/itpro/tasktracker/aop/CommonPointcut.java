package su.itpro.tasktracker.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CommonPointcut {

  @Pointcut("within(su.itpro.tasktracker.service.*Service)")
  public void isServiceLayer() {
  }

}
