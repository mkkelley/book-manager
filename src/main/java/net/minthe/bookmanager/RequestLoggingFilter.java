package net.minthe.bookmanager;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Created by Michael Kelley on 9/12/2020 */
@Component
public class RequestLoggingFilter implements Filter {
  private final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    var httpRequest = (HttpServletRequest) request;
    var host = httpRequest.getRemoteHost();
    var path = httpRequest.getServletPath();
    var qs = httpRequest.getQueryString();
    var query = qs == null ? "" : "?" + qs;
    var user =
        httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "";
    var session = httpRequest.getSession() != null ? httpRequest.getSession().getId() : "";
    var method = httpRequest.getMethod();

    Instant pre = Instant.now();

    chain.doFilter(request, response);

    Instant post = Instant.now();

    var httpResponse = (HttpServletResponse) response;
    var code = httpResponse.getStatus();
    if ("/actuator/health".equals(path)) {
      // too much logspam
      return;
    }
    logger.info(
        "{} {}{} - HTTP {} in {}ms [client={}, user={}, session={}]",
        method,
        path,
        query,
        code,
        Duration.between(pre, post).toMillis(),
        host,
        user,
        session);
  }
}
