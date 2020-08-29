package net.minthe.bookmanager.controllers;

import net.minthe.bookmanager.controllers.transport.CreateBookNoteRequest;
import net.minthe.bookmanager.models.BookNote;
import net.minthe.bookmanager.services.BookNoteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Michael Kelley on 8/29/2020
 */
@RequestMapping("/books/{bookId}/notes")
public class BookNoteController {
  private final BookNoteService bookNoteService;

  public BookNoteController(BookNoteService bookNoteService) {
    this.bookNoteService = bookNoteService;
  }

  @PostMapping
  public BookNote createBookNote(@RequestBody CreateBookNoteRequest request) {
    return bookNoteService.createBookNote(request);
  }
}
