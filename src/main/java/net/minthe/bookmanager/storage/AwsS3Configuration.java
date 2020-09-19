package net.minthe.bookmanager.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Michael Kelley on 9/19/2020
 */
@Configuration
class AwsS3Configuration {

  @Bean
  AmazonS3 getAmazonS3(AwsS3Settings settings) {
    var credentials = new BasicAWSCredentials(settings.getAccessKeyId(), settings.getSecretKey());
    var provider = new AWSStaticCredentialsProvider(credentials);
    return AmazonS3ClientBuilder.standard()
        .withCredentials(provider)
        .withRegion(settings.getRegion())
        .build();
  }
}
