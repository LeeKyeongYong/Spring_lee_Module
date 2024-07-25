package com.krstudy.kapi.com.krstudy.kapi.global.init

import com.krstudy.kapi.com.krstudy.kapi.domain.member.service.MemberService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.transaction.annotation.Transactional
import java.util.stream.IntStream

@Configuration
@Profile("!prod")
class NotProd(
    private val memberService: MemberService,
    private val postService: PostService
) {

    private val log = LoggerFactory.getLogger(NotProd::class.java)

    @Bean
    @Order(3)
    fun initNotProd(): ApplicationRunner {
        return ApplicationRunner { args ->
           // if (memberService.findByUsername("user1").isPresent) return@ApplicationRunner

            // findByUsername이 Member?를 반환한다고 가정
            if (memberService.findByUsername("user1") != null) return@ApplicationRunner

            work1()
        }
    }

    @Transactional
    fun work1() {
        val memberUser1 = memberService.join("user1", "1234").data
        val memberUser2 = memberService.join("user2", "1234").data
        val memberUser3 = memberService.join("user3", "1234").data
        val memberUser4 = memberService.join("user4", "1234").data

        val post1 = postService.write(memberUser1, "제목 1", "내용 1", true)
        val post2 = postService.write(memberUser1, "제목 2", "내용 2", true)
        val post3 = postService.write(memberUser1, "제목 3", "내용 3", false)
        val post4 = postService.write(memberUser1, "제목 4", "내용 4", true)

        val post5 = postService.write(memberUser2, "제목 5", "내용 5", true)
        val post6 = postService.write(memberUser2, "제목 6", "내용 6", false)

        IntStream.rangeClosed(7, 50).forEach { i ->
            postService.write(memberUser3, "제목 $i", "내용 $i", true)
        }

        postService.like(memberUser2, post1)
        postService.like(memberUser3, post1)
        postService.like(memberUser4, post1)

        postService.like(memberUser2, post2)
        postService.like(memberUser3, post2)

        postService.like(memberUser2, post3)
    }
}