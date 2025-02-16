package com.study.nextspring.domain.adm.controller;

import com.study.nextspring.domain.member.dto.MemberWithUsernameDto;
import com.study.nextspring.domain.member.entity.Member;
import com.study.nextspring.domain.member.service.MemberService;
import com.study.nextspring.global.base.MemberSearchKeywordTypeV1;
import com.study.nextspring.global.base.dto.PageDto;
import com.study.nextspring.global.httpsdata.RespData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/adm/members")
@RequiredArgsConstructor
@Tag(name = "ApiV1MemberController", description = "API 관리자용 회원 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class ApiV1AdmMemberController {
    private final MemberService memberService;


    @GetMapping
    @Transactional(readOnly = true)
    @Operation(summary = "회원 다건 조회")
    public PageDto<MemberWithUsernameDto> items(
            @RequestParam(name = "searchKeywordType", defaultValue = "username") MemberSearchKeywordTypeV1 searchKeywordType,
            @RequestParam(name = "searchKeyword", defaultValue = "") String searchKeyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize
    ) {
        return new PageDto<>(
                memberService.findByPaged(searchKeywordType, searchKeyword, page, pageSize)
                        .map(MemberWithUsernameDto::new)
        );
    }


    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    @Operation(summary = "회원 단건 조회")
    public MemberWithUsernameDto item(
            @PathVariable(name = "id") long id  // name 속성 추가
    ) {
        return new MemberWithUsernameDto(
                memberService.findById(id).get()
        );
    }
}