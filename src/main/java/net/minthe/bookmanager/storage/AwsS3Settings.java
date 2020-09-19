package net.minthe.bookmanager.storage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Michael Kelley on 9/19/2020
 */
@Getter
@Setter
@NoArgsConstructor
@ConfigurationProperties(prefix = "net.minthe.book-manager.s3")
@Component
class AwsS3Settings {

  private String bucket;
  private String accessKeyId;
  private String secretKey;
  private String region;
}
