package net.minthe.bookmanager.controllers;

import java.util.List;
import net.minthe.bookmanager.models.Book;
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
  public Book addTags(@PathVariable Long bookId, @RequestBody List<String> tags) {
    return bookService.addTags(bookId, tags);
  }

  @DeleteMapping("{tag}")
  public Book removeTag(@PathVariable Long bookId, @PathVariable String tag) {
    return bookService.removeTag(bookId, tag);
  }
}
