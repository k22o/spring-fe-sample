package com.example.springfesample.service;

import java.util.List;

import com.example.springfesample.domain.Todo;
import com.example.springfesample.infrastructure.TodoRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link TodoService} の単体テスト。{@link TodoRepository} はモック化する。
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({"NonAsciiCharacters"})
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Nested
    class FindAll {

        @Test
        void リポジトリから取得したやることの一覧をそのまま返す() {
            final List<Todo> todos = List.of(new Todo(1L, "掃除"), new Todo(2L, "洗濯"));
            when(todoRepository.findAll()).thenReturn(todos);

            final List<Todo> result = todoService.findAll();

            assertThat(result).containsExactly(new Todo(1L, "掃除"), new Todo(2L, "洗濯"));
        }
    }

    @Nested
    class Add {

        @Test
        void リポジトリにタイトルを渡して保存する() {
            todoService.add("掃除");

            verify(todoRepository).save("掃除");
        }
    }
}
