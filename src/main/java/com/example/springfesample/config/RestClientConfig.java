package com.example.springfesample.config;

import java.time.Duration;

import org.jspecify.annotations.NullMarked;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/**
 * 外部 Todo API 呼び出し用の {@link RestClient} を構成する設定クラス。
 */
@Configuration
@NullMarked
public class RestClientConfig {

    /** 外部 Todo API のベース URL。 */
    @Value("${external.todo-api.base-url:https://example.com}")
    private String baseUrl;

    /** 外部 Todo API への接続タイムアウト時間。 */
    @Value("${external.todo-api.connect-timeout:3s}")
    private Duration connectTimeout;

    /** 外部 Todo API からの読み取りタイムアウト時間。 */
    @Value("${external.todo-api.read-timeout:5s}")
    private Duration readTimeout;

    /**
     * 外部 Todo API 呼び出し用の {@link RestClient} を生成する。
     *
     * @return 構成済みの {@link RestClient}
     */
    @Bean
    public RestClient todoApiRestClient() {
        return  RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(buildRequestFactory())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * 接続・読み取りタイムアウトを設定した {@link ClientHttpRequestFactory} を生成する。
     *
     * @return 構成済みの {@link ClientHttpRequestFactory}
     */
    private ClientHttpRequestFactory buildRequestFactory() {
        final HttpClientSettings httpClientSettings = HttpClientSettings.defaults()
                .withConnectTimeout(connectTimeout)
                .withReadTimeout(readTimeout);
        return ClientHttpRequestFactoryBuilder.detect().build(httpClientSettings);
    }
}
