package net.minthe.bookmanager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@Profile("prod")
public class MvcConfig implements WebMvcConfigurer {
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/**")
        .addResourceLocations("classpath:/static/")
        .resourceChain(true)
        .addResolver(
            new PathResourceResolver() {
              @Override
              protected Resource getResource(String resourcePath, Resource location)
                  throws IOException {
                Resource requestedResource = location.createRelative(resourcePath);
                return requestedResource.exists() && requestedResource.isReadable()
                    ? requestedResource
                    : new ClassPathResource("/static/index.html");
              }
            });

    registry.addResourceHandler("**.js", "**.css")
        .addResourceLocations("classpath:/static/")
        .setCacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
        .resourceChain(true);
  }
}
