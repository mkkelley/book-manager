package net.minthe.bookmanager.services;

import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.CreateBookNoteRequest;
import net.minthe.bookmanager.models.BookNote;
import net.minthe.bookmanager.repositories.BookNoteRepository;
import org.springframework.stereotype.Service;

/** Created by Michael Kelley on 8/29/2020 */
@Service
public class BookNoteService {
  private final BookNoteRepository bookNoteRepository;
  private final AuthService authService;

  public BookNoteService(BookNoteRepository bookNoteRepository, AuthService authService) {
    this.bookNoteRepository = bookNoteRepository;
    this.authService = authService;
  }

  public BookNote createBookNote(CreateBookNoteRequest request) {
    var id = request.getId();
    var bookNote = new BookNote();
    bookNote.setId(id != null ? id : UUID.randomUUID());
    bookNote.setBookId(request.getBookId());
    bookNote.setUsername(authService.isAdmin() ? request.getUsername() : authService.getUsername());
    bookNote.setNotes(request.getNotes());
    return bookNoteRepository.save(bookNote);
  }
}
