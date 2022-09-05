package com.afba.imageplus.configuration;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorPageFilterConfig {
  @Bean
  public ErrorPageFilter errorPageFilter() {
    return new ErrorPageFilter();
  }

  @Bean
  public FilterRegistrationBean<ErrorPageFilter> disableSpringBootErrorFilter(ErrorPageFilter filter) {
    var filterRegistrationBean = new FilterRegistrationBean<>(filter);
    filterRegistrationBean.setFilter(filter);
    filterRegistrationBean.setEnabled(false);
    return filterRegistrationBean;
  }
}
