package org.study.jqboot.domain.board.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.study.jqboot.domain.board.entity.Reply;

import java.util.List;
import static org.jooq.impl.DSL.currentTimestamp;

@Repository
@RequiredArgsConstructor
public class ReplyRepository {
    /*private final DSLContext dsl;

    public boolean checkReplyPassword(Integer no, String password) {
        return dsl.selectCount()
                .from(REPLY2)
                .where(REPLY2.NO.eq(no))
                .and(REPLY2.PASSWORD.eq(password))
                .fetchOneInto(Integer.class) > 0;
    }

    public void addReply(Reply reply) {
        dsl.insertInto(REPLY2)
                .set(REPLY2.WRITER, reply.getWriter())
                .set(REPLY2.PASSWORD, reply.getPassword())
                .set(REPLY2.MEMO, reply.getMemo())
                .set(REPLY2.WDATE, currentTimestamp())
                .set(REPLY2.REF_NO, reply.getRefNo())
                .execute();
    }

    public void removeReply(Integer no) {
        dsl.deleteFrom(REPLY2)
                .where(REPLY2.NO.eq(no))
                .execute();
    }

    public List<Reply> getAllRepliesByNo(Integer refNo) {
        return dsl.selectFrom(REPLY2)
                .where(REPLY2.REF_NO.eq(refNo))
                .orderBy(REPLY2.NO)
                .fetchInto(Reply.class);
    }
    */
}