package com.fly.clstudy.sur.dto;

import com.fly.clstudy.sur.entity.Surl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import static lombok.AccessLevel.PROTECTED;
@NoArgsConstructor(access = PROTECTED)
@Getter
public class SurlDto {
    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long authorId;
    private String authorName;
    private String body;
    private String url;
    private long count;

    public SurlDto(Surl surl) {
        this.id = surl.getId();
        this.createDate = surl.getCreateDate();
        this.modifyDate = surl.getModifyDate();
        this.authorId = surl.getAuthor().getId();
        this.authorName = surl.getAuthor().getUsername();
        this.body = surl.getBody();
        this.url = surl.getUrl();
        this.count = surl.getCount();
    }
}
