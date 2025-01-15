package org.study.jqboot.domain.board.repository;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import org.study.jqboot.domain.board.entity.Board;
import org.study.jqboot.domain.board.entity.PageHelper;

import java.util.List;
import static org.jooq.impl.DSL.*;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
   /* private final DSLContext dsl;

    public Integer getSequenceNo() {
        return dsl.select(BOARD4.NO)
                .from(BOARD4)
                .orderBy(BOARD4.NO.desc())
                .limit(1)
                .fetchOneInto(Integer.class);
    }

    public List<Board> getAllBoardByPage(PageHelper pageHelper) {
        return dsl.select()
                .from(BOARD4)
                .orderBy(BOARD4.GROUP_NO.desc(), BOARD4.INDENT_IN_GROUP.asc())
                .limit(pageHelper.getStop() - pageHelper.getStart() + 1)
                .offset(pageHelper.getStart() - 1)
                .fetchInto(Board.class);
    }

    public Board getBoardByNo(Integer no) {
        return dsl.selectFrom(BOARD4)
                .where(BOARD4.NO.eq(no))
                .fetchOneInto(Board.class);
    }

    public Integer getBoardTotal() {
        return dsl.selectCount()
                .from(BOARD4)
                .fetchOneInto(Integer.class);
    }

    public boolean checkBoardPassword(Integer no, String password) {
        return dsl.selectCount()
                .from(BOARD4)
                .where(BOARD4.NO.eq(no))
                .and(BOARD4.PASSWORD.eq(password))
                .fetchOneInto(Integer.class) > 0;
    }

    public void addBoard(Board board) {
        dsl.insertInto(BOARD4)
                .set(BOARD4.NO, board.getNo())
                .set(BOARD4.WRITER, board.getWriter())
                .set(BOARD4.PASSWORD, board.getPassword())
                .set(BOARD4.TITLE, board.getTitle())
                .set(BOARD4.CONTENT, board.getContent())
                .set(BOARD4.WDATE, currentTimestamp())
                .set(BOARD4.READ_CNT, board.getReadCnt())
                .set(BOARD4.REPLY_CNT, board.getReplyCnt())
                .set(BOARD4.GROUP_NO, board.getGroupNo())
                .set(BOARD4.SEQUENCE_IN_GROUP, board.getSequenceInGroup())
                .set(BOARD4.INDENT_IN_GROUP, board.getIndentInGroup())
                .set(BOARD4.REF_NO, board.getRefNo())
                .execute();
    }

    public void modifyBoard(Board board) {
        dsl.update(BOARD4)
                .set(BOARD4.TITLE, board.getTitle())
                .set(BOARD4.CONTENT, board.getContent())
                .set(BOARD4.WRITER, board.getWriter())
                .set(BOARD4.PASSWORD, board.getPassword())
                .where(BOARD4.NO.eq(board.getNo()))
                .execute();
    }

    public void removeBoard(Integer no) {
        dsl.deleteFrom(BOARD4)
                .where(BOARD4.NO.eq(no))
                .execute();
    }

    public void raiseLookup(Integer no) {
        dsl.update(BOARD4)
                .set(BOARD4.READ_CNT, BOARD4.READ_CNT.add(1))
                .where(BOARD4.NO.eq(no))
                .execute();
    }*/
}