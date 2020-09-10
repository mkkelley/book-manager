package net.minthe.bookmanager;

import brave.Span;
import brave.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Aspect
@Component
@ConditionalOnProperty(value = "spring.sleuth.enabled", havingValue = "true")
public class JdbcSpanAspect {

  private final Tracer tracer;

  public JdbcSpanAspect(Tracer tracer) {
    this.tracer = tracer;
  }

  @Pointcut("within(@org.springframework.stereotype.Repository *)")
  public void repositoryClassMethods() {
  }

  @Pointcut("execution(* org.springframework.data.repository.CrudRepository.*(..))")
  public void crudRepositoryMethods() {
  }

  @Around("repositoryClassMethods() || crudRepositoryMethods()")
  public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
    Span jdbcSpan = this.tracer.nextSpan().name(pjp.getSignature().getName()).start();

    try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(jdbcSpan)) {
      return pjp.proceed();
    } catch (RuntimeException | Error e) {
      jdbcSpan.error(e);
      throw e;
    } finally {
      jdbcSpan.finish();
    }
  }
}
