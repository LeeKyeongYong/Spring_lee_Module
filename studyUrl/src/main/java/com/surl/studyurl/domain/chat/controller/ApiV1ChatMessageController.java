package com.surl.studyurl.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chatMessages")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiV1ChatMessageController {
}
