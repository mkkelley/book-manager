package net.minthe.bookmanager.controllers;

import net.minthe.bookmanager.models.ApplicationConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigController {
  private final ApplicationConfig config;

  public ConfigController(ApplicationConfig config) {
    this.config = config;
  }

  @GetMapping("/config")
  public ApplicationConfig getConfig() {
    return config;
  }
}
