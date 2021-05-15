package net.minthe.bookmanager;

import java.util.Collections;
import javax.sql.DataSource;
import net.minthe.bookmanager.models.User;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3600)
@EnableJpaAuditing
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BookManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookManagerApplication.class, args);
  }

  @Bean
  @Profile("!prod")
  public HttpSessionIdResolver httpSessionIdResolver() {
    var serializer = new DefaultCookieSerializer();
    var resolver = new CookieHttpSessionIdResolver();
    resolver.setCookieSerializer(serializer);
    return resolver;
  }

  @Bean
  @Profile("prod")
  public HttpSessionIdResolver prodSessionIdResolver() {
    var serializer = new DefaultCookieSerializer();
    serializer.setSameSite(SameSiteCookies.STRICT.getValue());
    serializer.setUseSecureCookie(true);
    var resolver = new CookieHttpSessionIdResolver();
    resolver.setCookieSerializer(serializer);
    return resolver;
  }

  @Bean
  public CommonsRequestLoggingFilter loggingFilter() {
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeClientInfo(true);
    filter.setIncludeQueryString(true);
    return filter;
  }

  @Bean
  public UserDetailsService userDetailsService(DataSource dataSource) {
    return new JdbcUserDetailsManager(dataSource);
  }

  @Bean
  public AuditorAware<User> auditorAware() {
    return new AuditorUser();
  }

  @Configuration()
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Value("${app.remember-me-key}")
    private String rememberMeKey;

    public SecurityConfiguration(UserDetailsService userDetailsService) {
      this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      if (rememberMeKey == null || "".equals(rememberMeKey)) {
        throw new IllegalStateException("app.remember-me-key must be defined");
      }
      http.csrf()
          .disable()
          .httpBasic()
          .and()
          .authorizeRequests()
          .antMatchers("/index.html", "/", "/home", "/user", "/config", "/*.js", "/*.css")
          .permitAll()
          .antMatchers("/api/**")
          .authenticated()
          .and()
          .logout()
          .logoutUrl("/api/logout")
          .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
          .and()
          .rememberMe()
          .alwaysRemember(true)
          .userDetailsService(userDetailsService)
          .key(rememberMeKey);
      http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }
  }
}
