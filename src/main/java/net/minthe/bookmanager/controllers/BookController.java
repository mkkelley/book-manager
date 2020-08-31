package net.minthe.bookmanager.controllers;

import java.util.Optional;
import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.controllers.transport.BookDetailDto;
import net.minthe.bookmanager.controllers.transport.BookDto;
import net.minthe.bookmanager.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping()
  public Page<BookDto> getBooks(
      @RequestParam(required = false) Optional<String> search,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    var pageRequest = PageRequest.of(page, size);
    return search
        .map(s -> bookService.searchBooks(s, pageRequest))
        .orElseGet(() -> bookService.getBooks(pageRequest))
        .map(BookDto::new);
  }

  @GetMapping("{bookId}")
  public BookDetailDto getBook(@PathVariable Long bookId) {
    return new BookDetailDto(bookService.getBook(bookId));
  }

  @PostMapping()
  public BookDto addBook(@RequestBody AddBookRequest request) {
    var book = bookService.addBook(request);
    return new BookDto(book);
  }

  @DeleteMapping("{bookId}")
  public BookDto deleteBook(@PathVariable Long bookId) {
    return new BookDto(bookService.deleteBook(bookId));
  }
}
