package com.example.module_app_member.global.jpa.entity;

@MappedSuperclass
@Getter
@Setter(AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime extends BaseEntity {
    @CreatedDate
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }
}