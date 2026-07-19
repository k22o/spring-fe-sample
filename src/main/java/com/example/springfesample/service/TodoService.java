package com.example.springfesample.service;

import java.util.List;

import com.example.springfesample.domain.Todo;
import com.example.springfesample.infrastructure.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

/**
 * やることに関するユースケースを提供するサービス。
 */
@Service
@NullMarked
@RequiredArgsConstructor
public class TodoService {

    /** やることの永続化・取得を行うリポジトリ。 */
    private final TodoRepository todoRepository;

    /**
     * やることの一覧を取得する。
     *
     * @return やることのリスト
     */
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    /**
     * やることを新規登録する。
     *
     * @param title やることのタイトル
     */
    public void add(String title) {
        todoRepository.save(title);
    }
}
