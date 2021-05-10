package net.minthe.bookmanager.controllers;

import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.controllers.transport.BookDetailDto;
import net.minthe.bookmanager.controllers.transport.BookDto;
import net.minthe.bookmanager.controllers.transport.UpdateBookRequest;
import net.minthe.bookmanager.services.BookFilter;
import net.minthe.bookmanager.services.BookReadService;
import net.minthe.bookmanager.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookService bookService;
  private final BookReadService bookReadService;

  public BookController(BookService bookService, BookReadService bookReadService) {
    this.bookService = bookService;
    this.bookReadService = bookReadService;
  }

  @GetMapping()
  public Page<BookDto> getBooks(
      BookFilter filter,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    var pageRequest = PageRequest.of(page, size);
    var searchResult = bookService.searchBooks(filter, pageRequest);
    return searchResult.getBooks().map(b -> new BookDto(b, searchResult.getBookReads()));
  }

  @GetMapping("{bookId}")
  public BookDetailDto getBook(@PathVariable Long bookId) {
    var reads = bookReadService.getBookReadsForCurrentUser(bookId);
    return new BookDetailDto(bookService.getBook(bookId), reads);
  }

  @PostMapping()
  public BookDto addBook(@RequestBody AddBookRequest request) {
    var book = bookService.addBook(request);
    return new BookDto(book, bookReadService.getBookReadsForCurrentUser(book.getId()));
  }

  @PutMapping("{bookId}")
  public BookDto updateBook(@PathVariable Long bookId, @RequestBody UpdateBookRequest request) {
    request.setId(bookId);
    var book = bookService.updateBook(request);
    var reads = bookReadService.getBookReadsForCurrentUser(bookId);
    return new BookDto(book, reads);
  }

  @DeleteMapping("{bookId}")
  public BookDto deleteBook(@PathVariable Long bookId) {
    var reads = bookReadService.getBookReadsForCurrentUser(bookId);
    return new BookDto(bookService.deleteBook(bookId), reads);
  }
}
