package net.minthe.bookmanager.controllers.transport;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.BookNote;

/** Created by Michael Kelley on 8/29/2020 */
@Getter
@Setter
@NoArgsConstructor
public class BookNoteDto {
  private UUID id;
  private String username;
  private Long bookId;
  private String notes;
  private Instant createdAt;
  private String createdBy;

  public BookNoteDto(BookNote bookNote) {
    this.id = bookNote.getId();
    this.username = bookNote.getUsername();
    this.username = bookNote.getUsername();
    this.notes = bookNote.getNotes();
    this.createdAt = bookNote.getCreatedAt();
    this.createdBy = bookNote.getCreatedBy().getUsername();
  }
}
