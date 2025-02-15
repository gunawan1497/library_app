package net.javaguides.lms.repository;

import net.javaguides.lms.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title%")
    List<Book> findByTitleLike(@Param("title") String title);

    @Query("SELECT b FROM Book b WHERE b.author LIKE %:author%")
    List<Book> findByAuthorLike(@Param("author") String author);
}
