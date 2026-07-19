package com.example.springfesample.controller;

import java.util.List;

import com.example.springfesample.domain.Todo;
import com.example.springfesample.service.TodoService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * {@link TodoController} の単体テスト。{@link TodoService} はモック化する。
 */
@WebMvcTest(TodoController.class)
@SuppressWarnings({"NonAsciiCharacters"})
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Nested
    class Index {

        @Test
        void やることの一覧をモデルに設定してindexビューを返す() throws Exception {
            final List<Todo> todos = List.of(new Todo(1L, "掃除"), new Todo(2L, "洗濯"));
            when(todoService.findAll()).thenReturn(todos);

            mockMvc.perform(get("/"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("index"))
                    .andExpect(model().attribute("todos", todos))
                    .andExpect(header().string("Cache-Control", "max-age=30"));
        }
    }

    @Nested
    class Add {

        @Test
        void タイトルが空でない場合はやることを登録してトップページへリダイレクトする() throws Exception {
            mockMvc.perform(post("/todos").param("title", "掃除"))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"))
                    .andExpect(header().string("Cache-Control", "no-store"));

            verify(todoService).add("掃除");
        }

        @Test
        void タイトルが空白のみの場合はやることを登録せずトップページへリダイレクトする() throws Exception {
            mockMvc.perform(post("/todos").param("title", "   "))
                    .andExpect(status().isFound())
                    .andExpect(redirectedUrl("/"));

            verify(todoService, never()).add(any());
        }
    }
}
