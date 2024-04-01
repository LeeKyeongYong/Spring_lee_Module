package com.example.sb_search.domain.post.postDocument.repository;

import com.example.sb_search.domain.post.postDocument.document.PostDocument;
import com.example.sb_search.global.app.AppConfig;
import com.example.sb_search.global.meilisearch.MeilisearchConfig;
import com.example.sb_search.global.standard.UtBase;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Searchable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

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
        getIndex().resetSortableAttributesSettings();
        getIndex().updateSortableAttributesSettings(new String[]{"id"});
    }

    private String getIndexName() {
        String indexName = "post";

        if (AppConfig.isTest()) indexName += "Test";

        return indexName;
    }

    public List<PostDocument> findByOrderByIdDesc() {
        // 검색 파라미터 설정
        SearchRequest searchRequest =
                new SearchRequest("")
                        .setSort(new String[]{"id:desc"});

        // 문서 검색
        Searchable search = getIndex().search(searchRequest);

        return
                search
                        .getHits()
                        .stream()
                        .map(
                                hit -> UtBase.json.toObject(hit, PostDocument.class)
                        )
                        .toList();
    }
}