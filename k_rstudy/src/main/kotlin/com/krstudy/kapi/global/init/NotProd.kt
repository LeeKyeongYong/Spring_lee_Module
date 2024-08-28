package com.krstudy.kapi.global.init

import com.krstudy.kapi.domain.member.datas.M_Role
import com.krstudy.kapi.domain.member.service.MemberService
import com.krstudy.kapi.domain.post.service.PostService
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream

@Configuration
@Profile("prod")
class NotProd(
    private val memberService: MemberService,
    private val postService: PostService
) {

    private val log = LoggerFactory.getLogger(NotProd::class.java)

    @Bean
    @Order(3)
    fun initNotProd(): ApplicationRunner {
        return ApplicationRunner { args ->
            runBlocking {
                // findByUsername이 Member?를 반환한다고 가정
                val memberUser1 = memberService.findByUserid("user1")
                if (memberUser1 != null) return@runBlocking

                work1()
            }
        }
    }

    @Transactional
    fun work1() {
        runBlocking {
            val memberUser1 = memberService.join("user1","일반 사용자1","abc@lky.co.kr","1234", "").data
             val memberUser2 = memberService.join("user2", "일반 사용자2","abc@lky.co.kr","1234", "").data
            val memberUser3 = memberService.join("user3", "일반 사용자3","abc@lky.co.kr","1234", "").data
            val memberUser4 = memberService.join("user4", "일반 사용자4","abc@lky.co.kr","1234", "").data
            // M_Role을 직접 사용하는 대신, 이름으로 변환
            val memberUser5 = memberService.join("m_user01", "그룹 운영자", "abc@lky.co.kr","1234", M_Role.MANAGER.authority).data
            val memberUser6 = memberService.join("h_user01", "인사 담당자", "abc@lky.co.kr","1234", M_Role.HR.authority).data
            val memberUser7 = memberService.join("d_user5", "임시 관리자", "abc@lky.co.kr","1234", M_Role.ADMIN.authority).data

            val memberUser8 = memberService.join("h_user02", "헤드 헌터", "abc@lky.co.kr","1234","ROLE_HEADHUNTER").data
            val memberUser9 = memberService.join("d_user2", "인사 담당자", "abc@lky.co.kr","1234", "ROLE_HR").data


            if (memberUser1 == null || memberUser2 == null || memberUser3 == null || memberUser4 == null || memberUser5 == null || memberUser6 == null
                || memberUser7 == null
                || memberUser8 == null
                || memberUser9 == null) {
                return@runBlocking
            }


            val post1 = postService.write(memberUser1, "제목 1", "내용 1", true)
            val post2 = postService.write(memberUser1, "제목 2", "내용 2", true)
            val post3 = postService.write(memberUser1, "제목 3", "내용 3", false)
            val post4 = postService.write(memberUser1, "제목 4", "내용 4", true)

            val post5 = postService.write(memberUser2, "제목 5", "내용 5", true)
            val post6 = postService.write(memberUser2, "제목 6", "내용 6", false)

            IntStream.rangeClosed(7, 1000).forEach { i ->
                postService.write(memberUser3, "제목 $i", "내용 $i", true)
                postService.writeComment(memberUser1, post1, "안녕하세요! $i 댓글입니다. 잘부탁드립니다.")
                postService.writeComment(memberUser2, post2, "안녕하세요! $i 댓글입니다. 잘부탁드립니다.")
                postService.writeComment(memberUser3, post3, "안녕하세요! $i 댓글입니다. 잘부탁드립니다.")
                postService.writeComment(memberUser4, post4, "안녕하세요! $i 댓글입니다. 잘부탁드립니다.")
                postService.writeComment(memberUser1, post5, "안녕하세요! $i 댓글입니다. 잘부탁드립니다.")
            }

            val likePairs = listOf(
                Pair(memberUser2, post1),
                Pair(memberUser3, post1),
                Pair(memberUser4, post1),
                Pair(memberUser2, post2),
                Pair(memberUser3, post2),
                Pair(memberUser2, post3)
            )

            likePairs.forEach { (member, post) ->
                if (!postService.canLike(member, post)) {
                    println("이미 좋아요를 누른 사용자: ${member.userid}, 게시물: ${post.id}")
                } else {
                    postService.like(member, post)
                }
            }
        }
    }
}