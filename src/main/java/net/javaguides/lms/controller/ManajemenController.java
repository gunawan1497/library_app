package net.javaguides.lms.controller;

import net.javaguides.lms.dto.ResponseData;
import net.javaguides.lms.entity.Book;
import net.javaguides.lms.service.BookService;
import net.javaguides.lms.service.UserService;
import net.javaguides.lms.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/manajemen")
public class ManajemenController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @PostMapping("/{bookId}/borrow/{userId}")
    public ResponseEntity<ResponseData<Book>> borrowBook(@PathVariable Long bookId, @PathVariable Long userId) {
        if (!bookService.existsById(bookId)) {
            return ResponseUtil.createNotFoundResponse("Book not found for ID: " + bookId);
        }

        if (!userService.existsById(userId)) {
            return ResponseUtil.createNotFoundResponse("User not found for ID: " + userId);
        }

        Book book = bookService.findById(bookId);
        if (book.isBorrowed()) {
            return ResponseUtil.createErrorResponse("Book is already borrowed.");
        }

        Book borrowedBook = bookService.borrowBook(bookId, userId);
        return ResponseUtil.createSuccessResponse(borrowedBook, "Book borrowed successfully");
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<ResponseData<Book>> returnBook(@PathVariable Long bookId) {
        if (!bookService.existsById(bookId)) {
            return ResponseUtil.createNotFoundResponse("Book not found for ID: " + bookId);
        }

        if (!bookService.findById(bookId).isBorrowed()) {
            return ResponseUtil.createErrorResponse("Book is not borrowed.");
        }

        Book returnedBook = bookService.returnBook(bookId);
        return ResponseUtil.createSuccessResponse(returnedBook, "Book returned successfully");
    }
}