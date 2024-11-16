package com.krstudy.kapi.domain.popups.entity

/**
 * 팝업 수정 이력 엔티티
 */
@Entity(name = "PopupHistory")
class PopupHistoryEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "popup_id")
    val popup: PopupEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id")
    val editor: Member,

    @Column(nullable = false)
    val action: String, // CREATE, UPDATE, DELETE, ACTIVATE, DEACTIVATE

    @Column(columnDefinition = "TEXT")
    val changeDetails: String, // JSON 형태로 변경 내용 저장

    @Column(nullable = false)
    val actionDate: LocalDateTime = LocalDateTime.now()
) : BaseEntity()