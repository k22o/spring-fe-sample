package com.example.springfesample.infrastructure;

import java.util.List;

import com.example.springfesample.domain.Todo;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

/**
 * {@link RestClient} 経由で外部 TodoAPI を呼び出す {@link TodoRepository} の実装。
 */
@Repository
@NullMarked
@RequiredArgsConstructor
public class TodoRepositoryImpl implements TodoRepository {

    /** 一覧取得時に外部 API へ指定する取得件数の上限。 */
    private static final int FETCH_LIMIT = 10;
    /** タイトルが未設定だった場合に使用する空文字列。 */
    private static final String EMPTY_TITLE = "";

    /** 外部 TodoAPI 呼び出し用のクライアント。 */
    private final RestClient restClient;

    /**
     * 外部 TodoAPI からやることの一覧を取得する。
     *
     * @return やることのリスト
     */
    @Override
    public List<Todo> findAll() {
        List<ExternalTodo> externalTodos = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/todos")
                        .queryParam("_limit", FETCH_LIMIT)
                        .build())
                .retrieve()
                .requiredBody(new ParameterizedTypeReference<List<ExternalTodo>>() {});
        return externalTodos.stream()
                .map(external -> new Todo(external.id(), toNonNullTitle(external.title())))
                .toList();
    }

    /**
     * 外部 TodoAPI へやることを新規登録する。
     *
     * @param title やることのタイトル
     * @return 登録されたやること
     */
    @Override
    public Todo save(String title) {
        ExternalTodo response = restClient.post()
                .uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ExternalTodo(0, title, false))
                .retrieve()
                .requiredBody(ExternalTodo.class);
        return new Todo(response.id(), toNonNullTitle(response.title()));
    }

    /**
     * タイトルが {@code null} の場合に空文字列へ変換する。
     *
     * @param title 変換対象のタイトル
     * @return {@code null} でないタイトル
     */
    private static String toNonNullTitle(@Nullable String title) {
        return title != null ? title : EMPTY_TITLE;
    }

    /**
     * 外部 TodoAPI とのやり取りに使用するデータ転送用レコード。
     *
     * @param id        やることの識別子
     * @param title     やることのタイトル
     * @param completed 完了フラグ
     */
    private record ExternalTodo(long id, @Nullable String title, boolean completed) {}
}
