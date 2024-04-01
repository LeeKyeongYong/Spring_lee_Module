package com.example.sb_search.domain.post.postDocument.repository;

import com.example.sb_search.domain.post.postDocument.document.PostDocument;
import com.example.sb_search.global.app.AppConfig;
import com.example.sb_search.global.meilisearch.MeilisearchConfig;
import com.example.sb_search.global.standard.UtBase;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.SearchRequest;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.Results;
import com.meilisearch.sdk.model.Searchable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.meilisearch.sdk.model.SearchResult;

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
        Searchable searchable  = getIndex().search(searchRequest);

        return
                searchable
                        .getHits()
                        .stream()
                        .map(
                                hit -> UtBase.json.toObject(hit, PostDocument.class)
                        )
                        .toList();
    }

    public Optional<PostDocument> findById(long id) {
        try {
            PostDocument document = getIndex().getDocument(String.valueOf(id), PostDocument.class);
            return Optional.ofNullable(document);
        } catch (MeilisearchException ignored) {
            System.out.println("ignored: "+ignored.toString());
        }

        return Optional.empty();
    }

    public Page<PostDocument> findByKw(String kw, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        SearchRequest searchRequest =
                new SearchRequest(kw)
                        .setAttributesToRetrieve(new String[]{"subject", "body"})
                        .setLimit(pageable.getPageSize())
                        .setOffset((int) pageable.getOffset())
                        .setShowMatchesPosition(true)
                        .setAttributesToHighlight(new String[]{"subject", "body"});

        if (startDate != null && endDate != null) {
            searchRequest
                    .setFilter(new String[]{
                            "createTimeStamp >= %d AND createTimeStamp <= %d"
                                    .formatted(
                                    UtBase.time.toTimeStamp(startDate),
                                    UtBase.time.toTimeStamp(endDate)
                            )
                    });
        }


        SearchResult searchResult = (SearchResult)getIndex().search(searchRequest);

        List<PostDocument> postDocuments = searchResult
                .getHits()
                .stream()
                .map(
                        hit -> {
                            Map<String, Object> formatted = (Map<String, Object>) hit.get("_formatted");
                            return UtBase.json.toObject(formatted, PostDocument.class);
                        }
                )
                .toList();

        return new PageImpl<>(postDocuments, pageable, searchResult.getEstimatedTotalHits());
    }

}