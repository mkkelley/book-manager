package net.minthe.bookmanager.controllers.transport;

import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.BookRead;

@Getter
@Setter
@NoArgsConstructor
public class BookReadDto {
  private UUID id;
  private String username;
  private long bookId;
  private Long started;
  private Long finished;
  private boolean audiobook;

  public BookReadDto(BookRead bookRead) {
    id = bookRead.getId();
    username = bookRead.getUsername();
    bookId = bookRead.getBookId();
    started = bookRead.getStarted().map(Instant::toEpochMilli).orElse(null);
    finished = bookRead.getFinished().map(Instant::toEpochMilli).orElse(null);
    audiobook = bookRead.isAudiobook();
  }
}
