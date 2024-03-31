package com.example.sb_search.domain.post.postDocument.repository;

import com.example.sb_search.domain.post.postDocument.document.PostDocument;
import com.example.sb_search.global.app.AppConfig;
import com.example.sb_search.global.meilisearch.MeilisearchConfig;
import com.example.sb_search.global.standard.UtBase;
import com.meilisearch.sdk.Index;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostDocumentRepository {
    private final MeilisearchConfig meilisearchConfig;
    private Index postIndex;

    private Index getIndex() {
        if (postIndex == null) postIndex = meilisearchConfig.meilisearchClient().index(getIndexName());

        return postIndex;
    }

    public void save(PostDocument postDocument) {
        getIndex().addDocuments(
                UtBase.json.toString(postDocument)
        );
    }

    public void clear() {
        getIndex().deleteAllDocuments();
    }

    private String getIndexName() {
        String indexName = "post";

        if (AppConfig.isTest()) indexName += "Test";

        return indexName;
    }
}