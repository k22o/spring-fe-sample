package com.example.springfesample.config;

import java.time.Duration;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link RestClientConfig} の単体テスト。{@code @Value} で注入されるフィールドはリフレクションで設定する。
 */
@SuppressWarnings({"NonAsciiCharacters"})
class RestClientConfigTest {

    private final RestClientConfig restClientConfig = new RestClientConfig();

    private void setProperties(final String baseUrl, final Duration connectTimeout, final Duration readTimeout) {
        ReflectionTestUtils.setField(restClientConfig, "baseUrl", baseUrl);
        ReflectionTestUtils.setField(restClientConfig, "connectTimeout", connectTimeout);
        ReflectionTestUtils.setField(restClientConfig, "readTimeout", readTimeout);
    }

    @Nested
    class TodoApiRestClient {

        @Test
        void 設定値をもとにRestClientを生成する() {
            setProperties("https://example.com", Duration.ofSeconds(3), Duration.ofSeconds(5));

            final RestClient restClient = restClientConfig.todoApiRestClient();

            assertThat(restClient).isNotNull();
        }
    }
}
