package net.minthe.bookmanager.controllers;

import java.util.List;
import net.minthe.bookmanager.controllers.transport.BookDto;
import net.minthe.bookmanager.services.BookReadService;
import net.minthe.bookmanager.services.BookTagService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books/{bookId}/tags")
public class BookTagController {
  private final BookTagService bookTagService;
  private final BookReadService bookReadService;

  public BookTagController(BookTagService bookTagService, BookReadService bookReadService) {
    this.bookTagService = bookTagService;
    this.bookReadService = bookReadService;
  }

  @PutMapping
  public BookDto addTags(@PathVariable Long bookId, @RequestBody List<String> tags) {
    return new BookDto(
        bookTagService.addTags(bookId, tags), bookReadService.getBookReadsForCurrentUser(bookId));
  }

  @DeleteMapping("{tag}")
  public BookDto removeTag(@PathVariable Long bookId, @PathVariable String tag) {
    return new BookDto(
        bookTagService.removeTag(bookId, tag), bookReadService.getBookReadsForCurrentUser(bookId));
  }
}
