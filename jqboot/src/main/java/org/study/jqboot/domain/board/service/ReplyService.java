package org.study.jqboot.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.jqboot.domain.board.entity.Reply;
import org.study.jqboot.domain.board.repository.ReplyRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReplyService {
    /*
    private final ReplyRepository replyRepository;

    public List<Reply> getAllRepliesByNo(Integer refNo) {
        return replyRepository.getAllRepliesByNo(refNo);
    }

    @Transactional
    public void addReply(Reply reply) {
        replyRepository.addReply(reply);
    }

    @Transactional
    public boolean removeReply(Integer no, String password) {
        if (replyRepository.checkReplyPassword(no, password)) {
            replyRepository.removeReply(no);
            return true;
        }
        return false;
    }
    */
}