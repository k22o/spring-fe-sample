package com.example.springfesample.config;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC のインターセプタ登録を行う設定クラス。
 */
@Configuration
@NullMarked
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    /** レスポンスヘッダーを付与するインターセプタ。 */
    private final HeaderInterceptor headerInterceptor;

    /**
     * {@link HeaderInterceptor} をインターセプタレジストリに登録する。
     *
     * @param registry インターセプタレジストリ
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(headerInterceptor);
    }
}
