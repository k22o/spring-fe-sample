package com.example.springfesample.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * すべてのレスポンスにセキュリティ関連 HTTP ヘッダーおよびキャッシュ制御ヘッダーを付与するインターセプタ。
 */
@Component
@NullMarked
public class HeaderInterceptor implements HandlerInterceptor {

    /** {@code Content-Security-Policy} ヘッダーの値。 */
    private static final String CONTENT_SECURITY_POLICY = String.join("; ",
            "default-src 'self'",
            "script-src 'self'",
            "style-src 'self'",
            "img-src 'self' data:",
            "object-src 'none'",
            "base-uri 'self'",
            "frame-ancestors 'none'");

    /** {@code Permissions-Policy} ヘッダーの値。 */
    private static final String PERMISSIONS_POLICY = String.join(", ",
            "camera=()",
            "microphone=()",
            "geolocation=()");

    /** 静的アセットとして扱うパスの接頭辞。 */
    private static final String STATIC_ASSET_PATH_PREFIX = "/css/";
    /** 静的アセットに設定する {@code Cache-Control} の値。 */
    private static final String STATIC_ASSET_CACHE_CONTROL = "public, max-age=86400";
    /** 動的コンテンツに設定する {@code Cache-Control} の値。 */
    private static final String DYNAMIC_CACHE_CONTROL = "no-store";

    /**
     * ハンドラ実行前にセキュリティ関連ヘッダーをレスポンスへ設定する。
     *
     * @param request  リクエスト
     * @param response レスポンス
     * @param handler  実行対象のハンドラ
     * @return 常に {@code true}（後続処理を継続する）
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");
        response.setHeader("Content-Security-Policy", CONTENT_SECURITY_POLICY);
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        response.setHeader("Permissions-Policy", PERMISSIONS_POLICY);
        return true;
    }

    /**
     * ハンドラ実行後、{@code Cache-Control} ヘッダーが未設定であればリクエストパスに応じた値を設定する。
     *
     * @param request      リクエスト
     * @param response     レスポンス
     * @param handler      実行されたハンドラ
     * @param modelAndView ハンドラが返した {@link ModelAndView}（{@code null} の場合あり）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            @Nullable ModelAndView modelAndView) {
        if (!response.containsHeader("Cache-Control")) {
            response.setHeader("Cache-Control", isStaticAsset(request) ? STATIC_ASSET_CACHE_CONTROL : DYNAMIC_CACHE_CONTROL);
        }
    }

    /**
     * リクエストパスが静的アセットを指しているかどうかを判定する。
     *
     * @param request リクエスト
     * @return 静的アセットへのリクエストであれば {@code true}
     */
    private boolean isStaticAsset(HttpServletRequest request) {
        return request.getRequestURI().startsWith(request.getContextPath() + STATIC_ASSET_PATH_PREFIX);
    }
}
