package net.minthe.bookmanager;

import java.util.Collections;
import javax.sql.DataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableRedisHttpSession
public class BookManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(BookManagerApplication.class, args);
  }

  @Bean
  public HttpSessionIdResolver httpSessionIdResolver() {
    return HeaderHttpSessionIdResolver.xAuthToken();
  }

  @Configuration()
  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
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
      http.cors()
          .configurationSource(corsConfigurationSource())
          .and()
          .httpBasic()
          .and()
          .authorizeRequests()
          .antMatchers("/index.html", "/", "/home", "/user")
          .permitAll()
          .anyRequest()
          .authenticated();
    }
  }
}
