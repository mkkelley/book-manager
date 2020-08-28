package net.minthe.bookmanager.controllers;

import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.AddBookReadRequest;
import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.controllers.transport.BookDto;
import net.minthe.bookmanager.controllers.transport.BookReadDto;
import net.minthe.bookmanager.controllers.transport.FinishBookReadRequest;
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
@RequestMapping("/books")
public class BookController {
  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @GetMapping()
  public Page<BookDto> getBooks(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    return bookService.getBooks(PageRequest.of(page, size)).map(BookDto::new);
  }

  @PostMapping()
  public BookDto addBook(@RequestBody AddBookRequest request) {
    var book = bookService.addBook(request);
    return new BookDto(book);
  }

  @PostMapping("{bookId}/reads")
  public BookReadDto addBookRead(
      @PathVariable Long bookId, @RequestBody AddBookReadRequest request) {
    request.setBookId(bookId);
    var bookRead = bookService.addBookRead(request);
    return new BookReadDto(bookRead);
  }

  @PostMapping("{bookId}/reads/{bookReadId}/finish")
  public BookReadDto finishBookRead(
      @PathVariable Long bookId,
      @PathVariable UUID bookReadId,
      @RequestBody FinishBookReadRequest request) {
    request.setId(bookReadId);
    return new BookReadDto(bookService.finishBookRead(request));
  }

  @DeleteMapping("{bookId}/reads/{bookReadId}")
  public BookReadDto deleteBookRead(@PathVariable Long bookId, @PathVariable UUID bookReadId) {
    return new BookReadDto(bookService.deleteBookRead(bookReadId));
  }
}
