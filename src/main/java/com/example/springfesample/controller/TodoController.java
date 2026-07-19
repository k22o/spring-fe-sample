package com.example.springfesample.controller;

import com.example.springfesample.service.TodoService;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * やること一覧の表示および登録を扱う MVC コントローラ。
 */
@Controller
@NullMarked
@RequiredArgsConstructor
public class TodoController {

    /** やることの一覧取得・登録を行うサービス。 */
    private final TodoService todoService;

    /**
     * やること一覧画面を表示する。
     *
     * @param model    ビューへ渡すモデル
     * @param response レスポンス（キャッシュ制御ヘッダー設定に使用）
     * @return テンプレート名 {@code "index"}
     */
    @GetMapping("/")
    public String index(Model model, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.maxAge(Duration.ofSeconds(30)).getHeaderValue());
        model.addAttribute("todos", todoService.findAll());
        return "index";
    }

    /**
     * やることを新規登録し、一覧画面へリダイレクトする。
     *
     * @param title やることのタイトル（空白のみの場合は登録しない）
     * @return 一覧画面へのリダイレクトレスポンス
     */
    @PostMapping("/todos")
    public ResponseEntity<Void> add(@RequestParam String title) {
        if (!title.isBlank()) {
            todoService.add(title);
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/"))
                .cacheControl(CacheControl.noStore())
                .build();
    }
}
