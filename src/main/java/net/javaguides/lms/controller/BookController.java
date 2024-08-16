package net.javaguides.lms.controller;

import jakarta.validation.Valid;
import net.javaguides.lms.dto.ResponseData;
import net.javaguides.lms.entity.Book;
import net.javaguides.lms.service.BookService;
import net.javaguides.lms.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String author) {
        if (title != null && !title.isEmpty()) {
            return bookService.findByTitleLike(title);
        }
        if (author != null && !author.isEmpty()) {
            return bookService.findByAuthorLike(author);
        }
        if (title == null && author == null) {
            return ResponseUtil.createNotFoundResponse("Please provide a title or author to search for books");
        }
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Book>> getBook(@PathVariable("id") Long id) {
        Book book = bookService.findById(id);

        if (book == null) {
            return ResponseUtil.createNotFoundResponse("Book not found for ID: " + id);
        }
        return ResponseUtil.createSuccessResponse(book, "Book retrieved successfully");
    }

    @PostMapping
    public ResponseEntity<ResponseData<Book>> addBook(@Valid @RequestBody Book book, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseUtil.createErrorResponse(errors);
        }
        return ResponseUtil.createSuccessResponse(bookService.save(book), "Book added successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Book>> updateBook(@Valid @PathVariable Long id, @Valid @RequestBody Book book, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseUtil.createErrorResponse(errors);
        }
        if (!bookService.existsById(id)) {
            return ResponseUtil.createNotFoundResponse("Book not found for ID: " + id);
        }
        book.setId(id);
        return ResponseUtil.createSuccessResponse(bookService.save(book), "Book updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteBook(@PathVariable Long id) {
        if (!bookService.existsById(id)) {
            return ResponseUtil.createNotFoundResponse("Book not found for ID: " + id);
        }
        bookService.deleteById(id);
        return ResponseUtil.createSuccessResponse(null, "Book deleted successfully");
    }

    // ... other endpoints ...
}