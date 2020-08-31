package net.minthe.bookmanager.controllers;

import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.AddBookReadRequest;
import net.minthe.bookmanager.controllers.transport.BookReadDto;
import net.minthe.bookmanager.controllers.transport.FinishBookReadRequest;
import net.minthe.bookmanager.services.BookReadService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by Michael Kelley on 8/29/2020 */
@RestController
@RequestMapping("/api/books/{bookId}/reads")
public class BookReadController {
  private final BookReadService bookReadService;

  public BookReadController(BookReadService bookReadService) {
    this.bookReadService = bookReadService;
  }

  @PostMapping
  public BookReadDto addBookRead(
      @PathVariable Long bookId, @RequestBody AddBookReadRequest request) {
    request.setBookId(bookId);
    var bookRead = bookReadService.addBookRead(request);
    return new BookReadDto(bookRead);
  }

  @PostMapping("{bookReadId}/finish")
  public BookReadDto finishBookRead(
      @PathVariable Long bookId,
      @PathVariable UUID bookReadId,
      @RequestBody FinishBookReadRequest request) {
    request.setId(bookReadId);
    return new BookReadDto(bookReadService.finishBookRead(request));
  }

  @DeleteMapping("{bookReadId}")
  public BookReadDto deleteBookRead(@PathVariable Long bookId, @PathVariable UUID bookReadId) {
    return new BookReadDto(bookReadService.deleteBookRead(bookReadId));
  }
}
