package net.minthe.bookmanager.repositories;

import net.minthe.bookmanager.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
  Page<Book> getBooksByOrderByCreatedAtDesc(Pageable pageable);
}
