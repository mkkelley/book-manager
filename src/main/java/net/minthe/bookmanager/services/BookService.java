package net.minthe.bookmanager.services;

import java.time.Instant;
import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.controllers.transport.UpdateBookRequest;
import net.minthe.bookmanager.exceptions.NotFoundException;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.models.BookSearchResult;
import net.minthe.bookmanager.repositories.BookReadRepository;
import net.minthe.bookmanager.repositories.BookRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorService authorService;
  private final AuthService authService;
  private final BookReadRepository bookReadRepository;

  public BookService(
      BookRepository bookRepository,
      AuthorService authorService,
      AuthService authService,
      BookReadRepository bookReadRepository) {
    this.bookRepository = bookRepository;
    this.authorService = authorService;
    this.authService = authService;
    this.bookReadRepository = bookReadRepository;
  }

  public Book getBook(Long id) {
    return this.bookRepository.findById(id).orElseThrow();
  }

  public Page<Book> getBooks(Pageable pageable) {
    return bookRepository.getBooksByOrderByCreatedAtDesc(pageable);
  }

  public BookSearchResult searchBooks(BookFilter filter, Pageable pageable) {
    var spec = new BookSpecification(filter);
    var books = bookRepository.findAll(spec, pageable);
    var bookReads =
        bookReadRepository.getBookReadsByUsernameAndBookIdInOrderByCreatedAtDesc(
            authService.getUsername(), books.map(Book::getId).toList());
    return new BookSearchResult(books, bookReads);
  }

  public Book addBook(AddBookRequest request) {
    var author = authorService.getOrCreate(request.getAuthorName());

    var book = new Book();
    book.setAuthor(author);
    book.setAuthorId(author.getId());
    book.setTitle(request.getTitle());
    book.setPublished(request.getPublished().map(Instant::ofEpochMilli).orElse(null));
    bookRepository.save(book);

    return bookRepository.findById(book.getId()).orElseThrow();
  }

  public Book updateBook(UpdateBookRequest request) {
    var book =
        bookRepository
            .findById(request.getId())
            .orElseThrow(() -> new NotFoundException(Book.class, request.getId()));

    var author = authorService.getOrCreate(request.getAuthorName());
    book.setAuthor(author);
    book.setAuthorId(author.getId());
    book.setTitle(request.getTitle());
    book.setPublished(request.getPublished().map(Instant::ofEpochMilli).orElse(null));
    return bookRepository.save(book);
  }

  public Book deleteBook(Long id) {
    var book = bookRepository.findById(id).orElseThrow();
    Hibernate.initialize(book.getAuthor());
    Hibernate.initialize(book.getTags());
    bookRepository.deleteById(id);
    return book;
  }
}
