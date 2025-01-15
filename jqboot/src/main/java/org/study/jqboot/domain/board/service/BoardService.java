package org.study.jqboot.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.study.jqboot.domain.board.entity.Board;
import org.study.jqboot.domain.board.entity.PageHelper;
import org.study.jqboot.domain.board.repository.BoardRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    /*private final BoardRepository boardRepository;

    public List<Board> getAllBoardByPage(PageHelper pageHelper) {
        return boardRepository.getAllBoardByPage(pageHelper);
    }

    public Board getBoardByNo(Integer no) {
        return boardRepository.getBoardByNo(no);
    }

    public Integer getBoardTotal() {
        return boardRepository.getBoardTotal();
    }

    @Transactional
    public void addBoard(Board board) {
        Integer newNo = boardRepository.getSequenceNo() + 1;
        board.setNo(newNo);
        board.setGroupNo(newNo);
        boardRepository.addBoard(board);
    }

    @Transactional
    public boolean modifyBoard(Board board) {
        if (boardRepository.checkBoardPassword(board.getNo(), board.getPassword())) {
            boardRepository.modifyBoard(board);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean removeBoard(Integer no, String password) {
        if (boardRepository.checkBoardPassword(no, password)) {
            boardRepository.removeBoard(no);
            return true;
        }
        return false;
    }

    @Transactional
    public void raiseLookup(Integer no) {
        boardRepository.raiseLookup(no);
    }*/
}