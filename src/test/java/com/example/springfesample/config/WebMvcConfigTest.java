package com.example.springfesample.config;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.Mockito.verify;

/**
 * {@link WebMvcConfig} の単体テスト。{@link InterceptorRegistry} はモック化する。
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"NonAsciiCharacters"})
class WebMvcConfigTest {

    private final HeaderInterceptor headerInterceptor = new HeaderInterceptor();

    @Mock
    private InterceptorRegistry interceptorRegistry;

    @Nested
    class AddInterceptors {

        @Test
        void HeaderInterceptorをインターセプタレジストリに登録する() {
            final WebMvcConfig webMvcConfig = new WebMvcConfig(headerInterceptor);

            webMvcConfig.addInterceptors(interceptorRegistry);

            verify(interceptorRegistry).addInterceptor(headerInterceptor);
        }
    }
}
