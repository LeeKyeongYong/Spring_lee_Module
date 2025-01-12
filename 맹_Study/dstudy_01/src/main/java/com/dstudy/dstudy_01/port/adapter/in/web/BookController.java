package com.dstudy.dstudy_01.port.adapter.in.web;

import com.dstudy.dstudy_01.global.exception.FileProcessingException;
import com.dstudy.dstudy_01.global.exception.InvalidFileTypeException;
import com.dstudy.dstudy_01.global.https.ReqData;
import com.dstudy.dstudy_01.port.application.in.BookUseCase;
import com.dstudy.dstudy_01.report.domain.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.dstudy.dstudy_01.port.domain.Book;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {

    private final ReqData rq;
    private final BookUseCase bookUseCase;

    @GetMapping("list")
    public String listBooks(@RequestParam(value = "page", defaultValue = "1") int page){
        List<Book> books = bookUseCase.getAllBooks(page);
        List<BookResponse> bookResponses = books.stream()
                .map(BookResponse::from)
                .collect(Collectors.toList());
        rq.setAttribute("books", books);
        return "domain/book/list";
    }

    @GetMapping("/{id}")
    public String viewBook(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage
    ) {
        Book book = bookUseCase.getBook(id);
        rq.setAttribute("book", book);
        rq.setAttribute("page", page);       // 목록으로 돌아갈 때 사용할 페이지 번호
        rq.setAttribute("currentPage", currentPage);  // PDF의 현재 페이지
        rq.setAttribute("lastPage", Math.min(book.getPageNum(), 10));
        return "domain/book/view";
    }

}
