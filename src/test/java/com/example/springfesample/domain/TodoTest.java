package com.example.springfesample.domain;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link Todo} の単体テスト。
 */
@SuppressWarnings({"NonAsciiCharacters"})
class TodoTest {

    @Nested
    class Constructor {

        @Test
        void 指定した識別子とタイトルを保持する() {
            final Todo todo = new Todo(1L, "やることを入力");

            assertThat(todo.id()).isEqualTo(1L);
            assertThat(todo.title()).isEqualTo("やることを入力");
        }
    }

    @Nested
    class EqualsAndHashCode {

        @Test
        void 識別子とタイトルが同じ場合は等価と判定する() {
            final Todo todo1 = new Todo(1L, "掃除");
            final Todo todo2 = new Todo(1L, "掃除");

            assertThat(todo1).isEqualTo(todo2).hasSameHashCodeAs(todo2);
        }

        @Test
        void タイトルが異なる場合は等価と判定しない() {
            final Todo todo1 = new Todo(1L, "掃除");
            final Todo todo2 = new Todo(1L, "洗濯");

            assertThat(todo1).isNotEqualTo(todo2);
        }
    }
}
