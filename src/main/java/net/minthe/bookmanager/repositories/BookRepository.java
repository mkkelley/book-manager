package net.minthe.bookmanager.repositories;

import net.minthe.bookmanager.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
  @EntityGraph(value = "Book.author")
  Page<Book> getBooksByOrderByCreatedAtDesc(Pageable pageable);

  @EntityGraph(value = "Book.author")
  Page<Book> getBooksByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
      String title, Pageable pageable);
}
