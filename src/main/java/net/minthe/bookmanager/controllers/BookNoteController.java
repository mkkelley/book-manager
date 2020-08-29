package net.minthe.bookmanager.controllers;

import net.minthe.bookmanager.controllers.transport.BookNoteDto;
import net.minthe.bookmanager.controllers.transport.CreateBookNoteRequest;
import net.minthe.bookmanager.services.BookNoteService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by Michael Kelley on 8/29/2020 */
@RestController
@RequestMapping("/books/{bookId}/notes")
public class BookNoteController {
  private final BookNoteService bookNoteService;

  public BookNoteController(BookNoteService bookNoteService) {
    this.bookNoteService = bookNoteService;
  }

  @PostMapping
  public BookNoteDto createBookNote(@RequestBody CreateBookNoteRequest request) {
    return new BookNoteDto(bookNoteService.createBookNote(request));
  }
}
