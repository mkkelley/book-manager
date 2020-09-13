package net.minthe.bookmanager.repositories;

import net.minthe.bookmanager.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CrudRepository<Book, Long>, JpaSpecificationExecutor<Book> {

  @Override
  @EntityGraph(value = "Book.author")
  Page<Book> findAll(Specification<Book> spec, Pageable pageable);

  @EntityGraph(value = "Book.author")
  Page<Book> getBooksByOrderByCreatedAtDesc(Pageable pageable);
}
