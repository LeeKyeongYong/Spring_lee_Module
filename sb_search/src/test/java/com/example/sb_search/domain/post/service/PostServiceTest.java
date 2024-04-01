package com.example.sb_search.domain.post.service;

import com.example.sb_search.domain.post.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PostServiceTest {

        @Autowired
        private PostService postService;

        @Test
        @DisplayName("findAll")
        void t1() {
            // 모든 포스트를 찾은 후 결과 검증
            List<Post> posts = postService.findAll();
            assertThat(posts).hasSize(17);

            // 순서대로 각 포스트 검증
            assertPost(posts.get(0), 3L, "subject3", "body3");
            assertPost(posts.get(1), 2L, "subject2", "body2");
            assertPost(posts.get(2), 1L, "subject1", "body1");

            assertPost(posts.get(posts.size() - 3), 3L, "subject3", "body3");
            assertPost(posts.get(posts.size() - 2), 2L, "subject2", "body2");
            assertPost(posts.get(posts.size() - 1), 1L, "subject1", "body1");
        }

        // 포스트 객체의 속성을 검증하는 헬퍼 메소드
        private void assertPost(Post post, Long expectedId, String expectedSubject, String expectedBody) {
            assertThat(post.getId()).isEqualTo(expectedId);
            assertThat(post.getSubject()).isEqualTo(expectedSubject);
            assertThat(post.getBody()).isEqualTo(expectedBody);
        }

    @Test
    @DisplayName("findById")
    void t2() {
        Post post = postService.findById(1).get();
        assertThat(post).isNotNull();

        // 순서대로 각 포스트 검증
        assertPost(post, 1L, "subject1", "body1");
    }
}
