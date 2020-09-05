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
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/** Created by Michael Kelley on 8/29/2020 */
@Entity
@Table(name = "users_book_notes")
@Getter
@Setter
public class BookNote extends Auditable {
  @Id private UUID id;
  private String username;

  @JoinColumn(name = "username", insertable = false, updatable = false)
  @ManyToOne
  private User user;

  @Column(name = "book_id")
  private Long bookId;

  @JoinColumn(name = "book_id", insertable = false, updatable = false)
  @ManyToOne
  private Book book;

  private String notes;

  @CreatedDate private Instant userCreatedAt;
  @LastModifiedDate private Instant userUpdatedAt;

  public Optional<Instant> getUserUpdatedAt() {
    return Optional.ofNullable(userUpdatedAt);
  }
}
