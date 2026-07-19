package com.example.springfesample.domain;

import org.jspecify.annotations.NullMarked;

/**
 * やることを表すドメインモデル。
 *
 * @param id    やることの識別子
 * @param title やることのタイトル
 */
@NullMarked
public record Todo(long id, String title) {
}
