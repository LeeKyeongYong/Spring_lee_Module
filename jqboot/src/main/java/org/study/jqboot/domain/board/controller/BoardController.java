package org.study.jqboot.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.study.jqboot.domain.board.entity.Board;
import org.study.jqboot.domain.board.entity.Reply;
import org.study.jqboot.domain.board.service.BoardService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final BoardService boardService;

    @GetMapping(value = {"/", "index"})
    public String getAllBoardByPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            Model model) {
        model.addAttribute("page", page);
        model.addAttribute("lastPage", boardService.getLastPage());
        model.addAttribute("boards", boardService.getAllBoardByPage(page));
        return "list";
    }

    @GetMapping("view")
    public String getBoardByNo(
            @RequestParam Integer no,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "n") String back,
            Model model) {
        if("n".equals(back)) {
            boardService.raiseLookup(no);
        }

        Board board = boardService.getBoardByNo(no);
        board.setContent("<pre>" + board.getContent() + "</pre>");

        model.addAttribute("board", board);
        model.addAttribute("page", page);
        model.addAttribute("replies", boardService.getAllRepliesByNo(no));
        model.addAttribute("reply", Reply.builder().build());
        return "view";
    }

    @GetMapping("edit")
    public String setupForInsert(
            @RequestParam(required = false) Integer no,
            @RequestParam(defaultValue = "1") Integer page,
            String action,
            Model model) {

        model.addAttribute("page", page);
        model.addAttribute("action", action);

        if("new".equals(action)) {
            model.addAttribute("title", "New Post");
            model.addAttribute("board", Board.builder().build());
        } else if("answer".equals(action)) {
            model.addAttribute("title", "Reply to Post");
            Board board = boardService.getBoardByNo(no);
            board.setWriter(null);
            board.setPassword(null);
            board.setTitle("[Reply] " + board.getTitle());
            board.setContent("\n\n========================Reply========================\n" + board.getContent());
            model.addAttribute("board", board);
        }
        return "edit";
    }

    @PostMapping("save")
    public String save(
            @RequestParam Integer no,
            @RequestParam(defaultValue = "1") Integer page,
            String action,
            Board board,
            Reply reply) {

        switch(action) {
            case "new":
                boardService.addBoard(board);
                return "redirect:/index?page=1";
            case "answer":
                board.setRefNo(no);
                boardService.addBoard(board);
                return "redirect:/index?page=" + page;
            case "update":
                board.setNo(no);
                boardService.modifyBoard(board);
                return "redirect:/view?no=" + no + "&page=" + page + "&back=y";
            case "reply":
                reply.setRefNo(no);
                boardService.addReply(reply);
                return "redirect:/view?no=" + no + "&page=" + page + "&back=y";
            default:
                throw new IllegalArgumentException("Invalid action type");
        }
    }

    @GetMapping("check")
    public String setupForCheckPassword(
            @RequestParam Integer no,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false) Integer rno,
            @RequestParam String action,
            Model model) {
        model.addAttribute("no", no);
        model.addAttribute("page", page);
        model.addAttribute("rno", rno);
        model.addAttribute("action", action);
        return "checkPassword";
    }

    @PostMapping("checkPassword")
    public String checkPassword(
            @RequestParam Integer no,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false) Integer rno,
            @RequestParam String action,
            @RequestParam String password,
            Model model) {

        switch(action) {
            case "update":
                if(boardService.checkBoardPassword(no, password)) {
                    model.addAttribute("title", "Edit Post");
                    model.addAttribute("no", no);
                    model.addAttribute("page", page);
                    model.addAttribute("action", action);
                    model.addAttribute("board", boardService.getBoardByNo(no));
                    return "edit";
                }
                throw new IllegalArgumentException("Invalid password");

            case "delete":
                if(boardService.checkBoardPassword(no, password)) {
                    boardService.removeBoard(no);
                    return "redirect:/index?page=" + page;
                }
                throw new IllegalArgumentException("Invalid password");

            case "del_repl":
                if(boardService.checkReplyPassword(rno, password)) {
                    boardService.removeReply(rno, no);
                    return "redirect:/view?no=" + no + "&page=" + page + "&back=y";
                }
                throw new IllegalArgumentException("Invalid password");

            default:
                throw new IllegalArgumentException("Invalid action type");
        }
    }
}