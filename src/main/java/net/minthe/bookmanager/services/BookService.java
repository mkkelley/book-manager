package net.minthe.bookmanager.services;

import java.time.Instant;
import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.models.Author;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.repositories.AuthorRepository;
import net.minthe.bookmanager.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;

  public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
  }

  public Page<Book> getBooks(Pageable pageable) {
    return bookRepository.getBooksByOrderByCreatedAtDesc(pageable);
  }

  public Book addBook(AddBookRequest request) {
    var authors = authorRepository.findByNameOrderById(request.getAuthorName());
    Author author;
    if (authors.size() == 0) {
      author = authorRepository.save(new Author(null, request.getAuthorName()));
    } else {
      author = authors.get(0);
    }

    var book = new Book();
    book.setAuthorId(author.getId());
    book.setTitle(request.getTitle());
    book.setPublished(request.getPublished().map(Instant::ofEpochMilli).orElse(null));
    bookRepository.save(book);

    return bookRepository.findById(book.getId()).get();
  }
}
