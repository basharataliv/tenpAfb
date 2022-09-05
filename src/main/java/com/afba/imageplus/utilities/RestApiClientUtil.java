package com.afba.imageplus.utilities;

import com.afba.imageplus.constants.EKDError;
import com.afba.imageplus.controller.exceptions.DomainException;
import com.afba.imageplus.service.ErrorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

@Component
public class RestApiClientUtil<T> extends DataMapperUtil {
    private final Logger logger = LoggerFactory.getLogger(RestApiClientUtil.class);

    private RestTemplate restTemplate;
    @Autowired
    protected ErrorService errorService;

    @Value("${life.pro.connect.timeout}")
    private Duration connectTimeout;

    @Value("${life.pro.read.timeout}")
    private Duration readTimeout;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.
                setConnectTimeout(connectTimeout).
                setReadTimeout(readTimeout).build();
        return this.restTemplate;
    }

    @Retryable(value = Exception.class, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public Optional<T> getApiCall(String url, String token, Class<T> resClass) {
        ResponseEntity<T> response = null;
        try {
            logger.info(url);
            // create headers
            HttpHeaders headers = new HttpHeaders();
            // set `accept` header
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(token);
            // set custom header
            // headers.set("x-request-src", "desktop");
            // build the request
            HttpEntity<String> entity = new HttpEntity<>("", headers);
            // use exchange method for HTTP call
            try {
                response = this.restTemplate.exchange(url, HttpMethod.GET, entity, resClass, 1);
            } catch (HttpStatusCodeException e) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
            }
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Exception in api call : ", e);
            return errorService.throwException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.LIFPRO407);
        }
    }

    @Retryable(value = { DomainException.class,
            ConnectException.class }, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public Optional<T> postApiCall(T Req, String url, String token, Class<T> resClass) {
        try {
            logger.info(url);
            ResponseEntity<T> response;
            // create headers
            HttpHeaders headers = new HttpHeaders();
            // set `request` header
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(token);
            HttpEntity<String> entity = new HttpEntity<>(mapToJson(Req), headers);
            // use exchange method for HTTP call
            try {
                response = restTemplate.exchange(url, HttpMethod.POST, entity, resClass, 1);
            } catch (HttpStatusCodeException e) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
            }
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
            }
        } catch (HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
            logger.error("Exception in api call : ", e);
            return errorService.throwException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.LIFPRO407);
        }
    }

    @Retryable(value = { DomainException.class,
            ConnectException.class }, maxAttemptsExpression = "${retry.maxAttempts}", backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public Optional<T> postApiCallAuthToken(MultiValueMap<String,String> req, String url, String token, Class resClass) {
        try {
            logger.info(url);
            ResponseEntity<T> response;
            // create headers
            HttpHeaders headers = new HttpHeaders();
            // set `request` header
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(req, headers);
            // use exchange method for HTTP call
            try {
                response = restTemplate.exchange(url, HttpMethod.POST, entity, resClass, 1);
            } catch (HttpStatusCodeException e) {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
            }
            if (response.getStatusCode() == HttpStatus.OK) {
                return Optional.ofNullable(response.getBody());
            } else {
                return errorService.throwException(HttpStatus.NOT_FOUND, EKDError.LIFPRO406);
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("Exception in api call : ", e);
            return errorService.throwException(HttpStatus.INTERNAL_SERVER_ERROR, EKDError.LIFPRO407);
        }
    }
}
