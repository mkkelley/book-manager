package net.minthe.bookmanager.controllers;

import java.util.List;
import net.minthe.bookmanager.controllers.transport.BookDto;
import net.minthe.bookmanager.services.BookService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books/{bookId}/tags")
public class BookTagController {

  private final BookService bookService;

  public BookTagController(BookService bookService) {
    this.bookService = bookService;
  }

  @PutMapping
  public BookDto addTags(@PathVariable Long bookId, @RequestBody List<String> tags) {
    return new BookDto(bookService.addTags(bookId, tags));
  }

  @DeleteMapping("{tag}")
  public BookDto removeTag(@PathVariable Long bookId, @PathVariable String tag) {
    return new BookDto(bookService.removeTag(bookId, tag));
  }
}
