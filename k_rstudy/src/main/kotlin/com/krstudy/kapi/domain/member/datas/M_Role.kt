package com.krstudy.kapi.com.krstudy.kapi.domain.member.datas

enum class M_Role(val authority: String) {
    MEMBER("ROLE_MEMBER"),       // 일반 사용자
    ADMIN("ROLE_ADMIN"),         // 관리자
    HEADHUNTER("ROLE_HEADHUNTER"), // 헤드헌터
    MANAGER("ROLE_MANAGER"),     // 매니저
    HR("ROLE_HR");               // 인사 담당자

    companion object {
        fun fromRoleType(roleType: String?): M_Role {
            return when (roleType) {
                "admin" -> ADMIN
                "headhunter" -> HEADHUNTER
                "manager" -> MANAGER
                "hr" -> HR
                else -> MEMBER
            }
        }

        fun getRoleType(role: M_Role): String {
            return role.name.toLowerCase()
        }
    }
}