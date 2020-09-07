package net.minthe.bookmanager;

import java.util.Collections;
import javax.sql.DataSource;
import net.minthe.bookmanager.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 3600)
@EnableJpaAuditing
public class BookManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookManagerApplication.class, args);
  }

  @Bean
  public HttpSessionIdResolver httpSessionIdResolver() {
    return HeaderHttpSessionIdResolver.xAuthToken();
  }

  @Bean
  public CommonsRequestLoggingFilter loggingFilter() {
    CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
    filter.setIncludeClientInfo(true);
    filter.setIncludeQueryString(true);
    return filter;
  }

  @Bean
  public AuditorAware<User> auditorAware() {
    return new AuditorUser();
  }

  @Configuration()
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Collections.singletonList("*"));
      configuration.setAllowedMethods(Collections.singletonList("*"));
      configuration.setAllowedHeaders(Collections.singletonList("*"));
      configuration.setExposedHeaders(Collections.singletonList("X-Auth-Token"));
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
      return new JdbcUserDetailsManager(dataSource);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http.csrf()
          .disable()
          .cors()
          .configurationSource(corsConfigurationSource())
          .and()
          .httpBasic()
          .and()
          .authorizeRequests()
          .antMatchers("/index.html", "/", "/home", "/user", "/config", "/*.js", "/*.css")
          .permitAll()
          .antMatchers("/api/**")
          .authenticated();
      http.exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint());
    }
  }
}
