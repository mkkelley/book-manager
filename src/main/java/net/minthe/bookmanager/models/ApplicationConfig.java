package net.minthe.bookmanager.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app")
@Component
@Getter
@Setter
public class ApplicationConfig {
  private String apiBaseUrl;
  private boolean registrationEnabled;
  private boolean storageEnabled;
}
