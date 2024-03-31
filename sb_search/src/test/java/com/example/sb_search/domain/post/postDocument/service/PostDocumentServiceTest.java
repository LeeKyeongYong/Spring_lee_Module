package com.example.sb_search.domain.post.postDocument.service;

import com.example.sb_search.domain.post.postDocument.document.PostDocument;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class PostDocumentServiceTest {
    @Autowired
    private PostDocumentService postDocumentService;

    @Test
    @DisplayName("findAll")
    void t1() {
        // 모든 포스트를 찾은 후 결과 검증
        List<PostDocument> posts = postDocumentService.findAll();

        System.out.println(posts);

        assertThat(posts).hasSize(3);
    }
}
