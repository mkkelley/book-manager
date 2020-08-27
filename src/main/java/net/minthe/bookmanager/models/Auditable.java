package net.minthe.bookmanager.models;

import java.time.Instant;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
  @Column(name = "created_at", insertable = false, updatable = false)
  @Generated(value = GenerationTime.INSERT)
  private Instant createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private Instant updatedAt;

  @JoinColumn(name = "created_by", updatable = false)
  @ManyToOne
  @CreatedBy
  private User createdBy;

  @JoinColumn(name = "updated_by")
  @ManyToOne
  @LastModifiedBy
  private User updatedBy;

  public Optional<User> getUpdatedBy() {
    return Optional.ofNullable(updatedBy);
  }

  public Optional<Instant> getUpdatedAt() {
    return Optional.ofNullable(updatedAt);
  }
}
