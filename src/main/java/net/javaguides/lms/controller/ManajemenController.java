package net.javaguides.lms.controller;

import net.javaguides.lms.dto.ResponseData;
import net.javaguides.lms.entity.Book;
import net.javaguides.lms.service.BookService;
import net.javaguides.lms.service.UserService;
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
        ResponseData<Book> responseData = new ResponseData<>();

        if (!bookService.existsById(bookId)) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book not found for ID: " + bookId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        if (!userService.existsById(userId)) {
            responseData.setStatus(false);
            responseData.getMessage().add("User not found for ID: " + userId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Book book = bookService.findById(bookId);
        if (book.isBorrowed()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book is already borrowed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Book borrowedBook = bookService.borrowBook(bookId, userId);
        responseData.setStatus(true);
        responseData.getMessage().add("Book borrowed successfully");
        responseData.setPayload(borrowedBook);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<ResponseData<Book>> returnBook(@PathVariable Long bookId) {
        ResponseData<Book> responseData = new ResponseData<>();

        if (!bookService.existsById(bookId)) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book not found for ID: " + bookId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        if (!bookService.findById(bookId).isBorrowed()) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book is not borrowed.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        Book returnedBook = bookService.returnBook(bookId);
        responseData.setStatus(true);
        responseData.getMessage().add("Book returned successfully");
        responseData.setPayload(returnedBook);
        return ResponseEntity.ok(responseData);
    }
}