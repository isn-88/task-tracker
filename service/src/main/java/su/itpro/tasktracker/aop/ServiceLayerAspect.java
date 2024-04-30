package su.itpro.tasktracker.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ServiceLayerAspect {

  @Before("su.itpro.tasktracker.aop.CommonPointcut.isServiceLayer()")
  public void addLoggingMethodArgs(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getSignature().getDeclaringTypeName();
    Object[] args = joinPoint.getArgs();
    log.info("invoked method: {} in class: {} with args: {}", methodName, className, args);
  }

  @AfterReturning(value = "su.itpro.tasktracker.aop.CommonPointcut.isServiceLayer()",
      returning = "result")
  public void addLoggingReturnValues(JoinPoint joinPoint, Object result) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getSignature().getDeclaringTypeName();
    log.info("invoked method: {} in class: {} with returning value: {}",
             methodName, className, result);
  }

  @AfterThrowing(value = "su.itpro.tasktracker.aop.CommonPointcut.isServiceLayer()",
      throwing = "ex")
  public void addLoggingAfterThrowing(JoinPoint joinPoint, Throwable ex) {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getSignature().getDeclaringTypeName();
    log.info("invoked method: {} in class: {} with throwing exception: {}",
             methodName, className, ex.getMessage());
  }

}
