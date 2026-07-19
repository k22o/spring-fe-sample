package com.example.springfesample.infrastructure;

import java.util.List;

import com.example.springfesample.domain.Todo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * {@link TodoRepositoryImpl} の単体テスト。{@link MockRestServiceServer} で外部 TodoAPI へのHTTP通信をモック化する。
 */
@SuppressWarnings({"NonAsciiCharacters"})
class TodoRepositoryImplTest {

    private MockRestServiceServer server;

    private TodoRepositoryImpl todoRepository;

    @BeforeEach
    void setUp() {
        final RestClient.Builder builder = RestClient.builder();
        server = MockRestServiceServer.bindTo(builder).build();
        todoRepository = new TodoRepositoryImpl(builder.build());
    }

    @AfterEach
    void tearDown() {
        server.verify();
    }

    @Nested
    class FindAll {

        @Test
        void 外部TodoAPIのレスポンスをやることのリストに変換して返す() {
            server.expect(requestTo("/todos?_limit=10"))
                    .andExpect(method(HttpMethod.GET))
                    .andRespond(withSuccess("""
                            [
                              { "id": 1, "title": "掃除", "completed": false },
                              { "id": 2, "title": "洗濯", "completed": true }
                            ]
                            """, MediaType.APPLICATION_JSON));

            final List<Todo> result = todoRepository.findAll();

            assertThat(result).containsExactly(new Todo(1L, "掃除"), new Todo(2L, "洗濯"));
        }

        @Test
        void 外部TodoAPIのタイトルがnullの場合は空文字に変換する() {
            server.expect(requestTo("/todos?_limit=10"))
                    .andRespond(withSuccess("""
                            [
                              { "id": 1, "title": null, "completed": false }
                            ]
                            """, MediaType.APPLICATION_JSON));

            final List<Todo> result = todoRepository.findAll();

            assertThat(result).containsExactly(new Todo(1L, ""));
        }
    }

    @Nested
    class Save {

        @Test
        void 外部TodoAPIへタイトルを送信して登録されたやることを返す() {
            server.expect(requestTo("/todos"))
                    .andExpect(method(HttpMethod.POST))
                    .andExpect(jsonPath("$.title").value("掃除"))
                    .andExpect(jsonPath("$.completed").value(false))
                    .andRespond(withSuccess("""
                            { "id": 3, "title": "掃除", "completed": false }
                            """, MediaType.APPLICATION_JSON));

            final Todo result = todoRepository.save("掃除");

            assertThat(result).isEqualTo(new Todo(3L, "掃除"));
        }

        @Test
        void 外部TodoAPIのタイトルがnullの場合は空文字に変換する() {
            server.expect(requestTo("/todos"))
                    .andRespond(withSuccess("""
                            { "id": 3, "title": null, "completed": false }
                            """, MediaType.APPLICATION_JSON));

            final Todo result = todoRepository.save("掃除");

            assertThat(result).isEqualTo(new Todo(3L, ""));
        }
    }
}
