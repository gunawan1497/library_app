package net.javaguides.lms.controller;

import jakarta.validation.Valid;
import net.javaguides.lms.dto.ResponseData;
import net.javaguides.lms.entity.Book;
import net.javaguides.lms.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<Book>> getBook(@PathVariable("id") Long id) {
        ResponseData<Book> responseData = new ResponseData<>();
        Book book = bookService.findById(id);

        if (book == null) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book not found for ID: " + id);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        responseData.setStatus(true);
        responseData.setPayload(book);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping
    public ResponseEntity<ResponseData<Book>> addBook(@Valid @RequestBody Book book, Errors errors) {
        ResponseData<Book> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        responseData.setStatus(true);
        responseData.getMessage().add("Book added successfully");
        responseData.setPayload(bookService.save(book));
        return ResponseEntity.ok(responseData);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseData<Book>> updateBook(@Valid @PathVariable Long id, @Valid @RequestBody Book book, Errors errors) {
        ResponseData<Book> responseData = new ResponseData<>();

        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        if (!bookService.existsById(id)) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book not found for ID: " + id);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        book.setId(id); // Ensure the book ID is set to the path variable ID
        responseData.setStatus(true);
        responseData.getMessage().add("Book updated successfully");
        responseData.setPayload(bookService.save(book));
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<Void>> deleteBook(@PathVariable Long id) {
        ResponseData<Void> responseData = new ResponseData<>();

        if (!bookService.existsById(id)) {
            responseData.setStatus(false);
            responseData.getMessage().add("Book not found for ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseData);
        }

        bookService.deleteById(id);
        responseData.setStatus(true);
        responseData.getMessage().add("Book deleted successfully");
        return ResponseEntity.ok(responseData);
    }

    // ... other endpoints ...
}