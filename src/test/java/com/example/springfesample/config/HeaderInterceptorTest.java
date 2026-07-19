package com.example.springfesample.config;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link HeaderInterceptor} の単体テスト。
 */
@SuppressWarnings({"NonAsciiCharacters"})
class HeaderInterceptorTest {

    private final HeaderInterceptor interceptor = new HeaderInterceptor();

    @Nested
    class PreHandle {

        @Test
        void レスポンスにセキュリティ関連ヘッダーを設定してtrueを返す() {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            final MockHttpServletResponse response = new MockHttpServletResponse();

            final boolean result = interceptor.preHandle(request, response, new Object());

            assertThat(result).isTrue();
            assertThat(response.getHeader("Strict-Transport-Security"))
                    .isEqualTo("max-age=31536000; includeSubDomains; preload");
            assertThat(response.getHeader("X-Content-Type-Options")).isEqualTo("nosniff");
            assertThat(response.getHeader("X-Frame-Options")).isEqualTo("DENY");
            assertThat(response.getHeader("Content-Security-Policy")).isEqualTo(String.join("; ",
                    "default-src 'self'",
                    "script-src 'self'",
                    "style-src 'self'",
                    "img-src 'self' data:",
                    "object-src 'none'",
                    "base-uri 'self'",
                    "frame-ancestors 'none'"));
            assertThat(response.getHeader("Referrer-Policy")).isEqualTo("strict-origin-when-cross-origin");
            assertThat(response.getHeader("Permissions-Policy")).isEqualTo("camera=(), microphone=(), geolocation=()");
        }

        @Test
        void Cache_Controlは設定しない() {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            final MockHttpServletResponse response = new MockHttpServletResponse();

            interceptor.preHandle(request, response, new Object());

            assertThat(response.getHeader("Cache-Control")).isNull();
        }
    }

    @Nested
    class PostHandle {

        @Test
        void Cache_Controlが未設定かつ静的アセットパスの場合はキャッシュを許可する値を設定する() {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/css/base.css");
            final MockHttpServletResponse response = new MockHttpServletResponse();

            interceptor.postHandle(request, response, new Object(), null);

            assertThat(response.getHeader("Cache-Control")).isEqualTo("public, max-age=86400");
        }

        @Test
        void Cache_Controlが未設定かつ静的アセットパス以外の場合はno_storeを設定する() {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/");
            final MockHttpServletResponse response = new MockHttpServletResponse();

            interceptor.postHandle(request, response, new Object(), null);

            assertThat(response.getHeader("Cache-Control")).isEqualTo("no-store");
        }

        @Test
        void Cache_Controlが既に設定されている場合は上書きしない() {
            final MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRequestURI("/");
            final MockHttpServletResponse response = new MockHttpServletResponse();
            response.setHeader("Cache-Control", "max-age=30");

            interceptor.postHandle(request, response, new Object(), null);

            assertThat(response.getHeader("Cache-Control")).isEqualTo("max-age=30");
        }
    }
}
