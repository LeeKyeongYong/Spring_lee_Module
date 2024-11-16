package com.krstudy.kapi.domain.popups.entity

/**
 * 팝업 템플릿 엔티티
 */
@Entity(name = "PopupTemplate")
class PopupTemplateEntity(
    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var content: String,

    @Column(nullable = false)
    var width: Int,

    @Column(nullable = false)
    var height: Int,

    @Column
    var backgroundColor: String?,

    @Column
    var borderStyle: String?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    var creator: Member,

    @Column(nullable = false)
    var isDefault: Boolean = false
) : BaseEntity()