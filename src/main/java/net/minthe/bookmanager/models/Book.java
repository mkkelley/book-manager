package net.minthe.bookmanager.models;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "books")
public class Book extends Auditable {
  @Id private Long id;
  private String title;

  @Column(name = "author_id")
  private int authorId;

  @JoinColumn(name = "author_id", insertable = false, updatable = false)
  @ManyToOne
  private Author author;

  private Instant published;
}
