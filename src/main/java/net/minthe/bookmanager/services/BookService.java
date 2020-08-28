package net.minthe.bookmanager.services;

import java.time.Instant;
import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.AddBookReadRequest;
import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.controllers.transport.FinishBookReadRequest;
import net.minthe.bookmanager.models.Author;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.models.BookRead;
import net.minthe.bookmanager.repositories.AuthorRepository;
import net.minthe.bookmanager.repositories.BookReadRepository;
import net.minthe.bookmanager.repositories.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final BookReadRepository bookReadRepository;

  public BookService(
      BookRepository bookRepository,
      AuthorRepository authorRepository,
      BookReadRepository bookReadRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.bookReadRepository = bookReadRepository;
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

  public BookRead addBookRead(AddBookReadRequest request) {
    var bookOpt = bookRepository.findById(request.getBookId());
    if (bookOpt.isEmpty()) {
      throw BadRequest.create(
          HttpStatus.BAD_REQUEST, "id does not exist", HttpHeaders.EMPTY, null, null);
    }

    var book = bookOpt.get();
    var newRead = new BookRead();
    newRead.setAudiobook(request.isAudiobook());
    newRead.setBookId(request.getBookId());
    newRead.setId(request.getId());
    newRead.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    newRead.setStarted(Instant.ofEpochMilli(request.getStarted()));

    return bookReadRepository.save(newRead);
  }

  public BookRead finishBookRead(FinishBookReadRequest request) {
    var bookReadOpt = bookReadRepository.findById(request.getId());
    if (bookReadOpt.isEmpty()) {
      throw BadRequest.create(
          HttpStatus.BAD_REQUEST, "id does not exist", HttpHeaders.EMPTY, null, null);
    }
    var bookRead = bookReadOpt.get();
    bookRead.setFinished(Instant.ofEpochMilli(request.getFinished()));
    return bookReadRepository.save(bookRead);
  }

  public BookRead deleteBookRead(UUID id) {
    var bookRead = bookReadRepository.findById(id).get();
    bookReadRepository.deleteById(id);
    return bookRead;
  }
}
