package net.minthe.bookmanager.models;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users_book_reads")
@Getter
@Setter
public class BookRead extends Auditable {
  @Id private UUID id;

  @JoinColumn(name = "username", insertable = false, updatable = false)
  @ManyToOne
  private User user;

  @Column(name = "username")
  private String username;

  @JoinColumn(name = "book_id", insertable = false, updatable = false)
  @ManyToOne
  private Book book;

  @Column(name = "book_id")
  private Long bookId;

  private Instant started;
  private Instant finished;
  private boolean audiobook;

  public Optional<Instant> getStarted() {
    return Optional.ofNullable(started);
  }

  public Optional<Instant> getFinished() {
    return Optional.ofNullable(finished);
  }
}
