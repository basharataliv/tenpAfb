package com.afba.imageplus.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ImageThreadingConfig {

  @Value("${image.download.core-pool-size:0}")
  private Integer downloadCorePoolSize;
  @Value("${image.download.max-pool-size:1}")
  private Integer downloadMaxPoolSize;
  @Value("${image.download.file.core-pool-size:0}")
  private Integer downloadFileCorePoolSize;
  @Value("${image.download.file.max-pool-size:1}")
  private Integer downloadFileMaxPoolSize;
  @Value("${image.conversion.core-pool-size:0}")
  private Integer conversionCorePoolSize;
  @Value("${image.conversion.max-pool-size:1}")
  private Integer conversionMaxPoolSize;

  @Bean("downloadFileTaskExecutor")
  public AsyncTaskExecutor downloadFileTaskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(downloadFileCorePoolSize);
    executor.setMaxPoolSize(downloadFileMaxPoolSize);
    return executor;
  }

  @Bean("downloadTaskExecutor")
  public AsyncTaskExecutor downloadTaskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(downloadCorePoolSize);
    executor.setMaxPoolSize(downloadMaxPoolSize);
    return executor;
  }

  @Bean("conversionTaskExecutor")
  public AsyncTaskExecutor conversionTaskExecutor() {
    var executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(conversionCorePoolSize);
    executor.setMaxPoolSize(conversionMaxPoolSize);
    return executor;
  }

}
