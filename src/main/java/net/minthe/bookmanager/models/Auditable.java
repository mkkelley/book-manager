package net.minthe.bookmanager.models;

import java.time.Instant;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Auditable {
  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @JoinColumn(name = "created_by", insertable = false, updatable = false)
  @ManyToOne
  private User createdBy;

  @JoinColumn(name = "updated_by")
  @ManyToOne
  private User updatedBy;

  public Optional<User> getUpdatedBy() {
    return Optional.ofNullable(updatedBy);
  }

  public Optional<Instant> getUpdatedAt() {
    return Optional.ofNullable(updatedAt);
  }
}
