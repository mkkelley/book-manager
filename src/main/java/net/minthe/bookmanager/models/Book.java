package net.minthe.bookmanager.models;

import java.time.Instant;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Getter
@Setter
@Table(name = "books")
public class Book extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(name = "author_id")
  private Long authorId;

  @JoinColumn(name = "author_id", insertable = false, updatable = false)
  @ManyToOne
  @Generated(value = GenerationTime.INSERT)
  private Author author;

  @OneToMany(mappedBy = "bookId")
  private List<BookRead> bookReads;

  private Instant published;
}
