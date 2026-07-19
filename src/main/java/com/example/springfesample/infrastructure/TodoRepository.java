package com.example.springfesample.infrastructure;

import com.example.springfesample.domain.Todo;

import java.util.List;
import org.jspecify.annotations.NullMarked;

/**
 * やることの永続化・取得を行うリポジトリ。
 */
@NullMarked
public interface TodoRepository {

    /**
     * やることの一覧を取得する。
     *
     * @return やることのリスト
     */
    List<Todo> findAll();

    /**
     * やることを新規登録する。
     *
     * @param title やることのタイトル
     * @return 登録されたやること
     */
    Todo save(String title);
}
