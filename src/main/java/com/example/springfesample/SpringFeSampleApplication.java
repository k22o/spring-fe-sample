package com.example.springfesample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot アプリケーションのエントリポイント。
 */
@SpringBootApplication
public class SpringFeSampleApplication {

    /**
     * アプリケーションを起動する。
     *
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SpringApplication.run(SpringFeSampleApplication.class, args);
    }

}
