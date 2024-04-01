package com.example.sb_search.global.initdata;

import com.example.sb_search.domain.post.postDocument.service.PostDocumentService;
import com.example.sb_search.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

@Profile("!prod")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class NotProd {
    @Autowired
    @Lazy
    private NotProd self;
    private final PostService postService;
    private final PostDocumentService postDocumentService;

    @Bean
    public ApplicationRunner initNotProd() {
        return args -> {
            if (postService.count() > 0) return;

            self.work1();
        };
    }

    @Transactional
    public void work1() {
        postDocumentService.clear();
        postService.write("subject1", "body1");
        postService.write("subject2", "body2");
        postService.write("subject3", "body3");

        postService.write("오늘의 운동 루틴 공유해요", "오늘은 유산소 위주로 30분 걷기와 함께 가벼운 스트레칭을 했어요. 여러분은 어떤 운동을 좋아하나요?");
        postService.write("최근 읽은 책 추천합니다!", "‘모든 순간의 의미’라는 책을 읽었는데, 일상의 소중함을 다시 한번 느낄 수 있었어요. 강추합니다!");
        postService.write("집에서 할 수 있는 취미 생활은?", "최근에는 집에서 할 수 있는 취미 생활을 찾고 있어요. 특히 그림 그리기나 요리 같은 걸 배워보고 싶네요.");
        postService.write("휴대폰 배터리 오래 쓰는 팁 있나요?", "요즘 휴대폰 배터리가 너무 빨리 닳는 것 같아요. 혹시 배터리를 조금이라도 더 오래 쓸 수 있는 팁이 있을까요?");
        postService.write("주말에 가볼 만한 조용한 카페 추천 부탁드려요", "친구들과 수다 떨면서 조용히 시간 보낼 수 있는 카페 찾고 있어요. 분위기 좋은 곳 있으면 추천해주세요!");
        postService.write("오늘 저녁 메뉴 추천해주세요", "매일 뭐 먹을지 고민되는데, 오늘 저녁엔 뭘 해먹는 게 좋을까요? 간단하면서 맛있는 메뉴 있으면 추천해주세요!");
        postService.write("최근에 본 영화 중 인상 깊었던 건?", "저는 ‘타임루프’ 장르의 영화를 최근에 봤는데 꽤 흥미로웠어요. 여러분이 최근에 본 영화 중 추천하고 싶은 게 있다면 공유해주세요!");

        postService.write("아침 일찍 일어나는 비결이 있나요?", "최근에 아침형 인간으로 바꾸려고 하는데, 매일 아침 일찍 일어나는 것이 쉽지 않네요. 아침에 쉽게 일어날 수 있는 비결이 있다면 공유해주세요!");
        postService.write("홈 카페 레시피 공유해요", "주말에 집에서 특별한 커피를 만들어 보고 싶어요. 집에서 쉽게 만들 수 있는 홈 카페 레시피가 있으면 추천해주세요!");
        postService.write("요가 시작하는 팁?", "최근에 건강을 위해 요가를 시작하려고 해요. 요가 초보자에게 유용한 팁이나 추천하는 요가 동작이 있나요?");
        postService.write("일상에서의 작은 행복 찾기", "요즘 같은 바쁜 일상에서 작은 행복을 찾는 것이 중요한 것 같아요. 여러분의 일상 속 작은 행복은 무엇인가요?");
        postService.write("집콕 생활, 영화 추천 부탁드려요", "주말에 집에서 영화 마라톤을 하려고 해요. 최근에 본 영화 중에서 가족이나 혼자 보기 좋은 영화 추천해주세요!");
        postService.write("간단하게 만들 수 있는 건강 간식 추천", "간식을 좋아하는데, 건강을 생각해서라도 집에서 간단하게 만들 수 있는 건강 간식이 있을까요? 공유해주시면 감사하겠습니다!");
        postService.write("효율적인 집안일 관리 방법", "주말마다 집안일로 바쁜데, 좀 더 효율적으로 집안일을 관리할 수 있는 방법이 있을까요? 팁이나 노하우가 있다면 공유해주세요!");
    }
}