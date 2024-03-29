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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

@Entity
@Getter
@Setter
@Table(name = "books")
@NamedEntityGraph(
    name = "Book.author",
    attributeNodes = {@NamedAttributeNode("author")})
public class Book extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(name = "author_id", nullable = false)
  private Long authorId;

  @JoinColumn(name = "author_id", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  @Generated(value = GenerationTime.INSERT)
  private Author author;

  private Instant published;

  @OneToMany(mappedBy = "bookId")
  @BatchSize(size = 20)
  @OrderBy("userCreatedAt ASC")
  private List<BookNote> bookNotes;

  @OneToMany(mappedBy = "bookId", orphanRemoval = true)
  @BatchSize(size = 20)
  private List<BookTag> tags;

  @OneToMany(mappedBy = "bookId")
  @BatchSize(size = 20)
  @OrderBy("createdAt DESC")
  private List<BookRead> bookReads;

  /**
   * bookReads is mapped on the entity to allow for querying, but accessing it directly is usually
   * not desired, as authorization filters are not applied.
   *
   * See BookReadRepository for methods to get the appropriate BookReads for a Book + User
   * combination.
   */
  public List<BookRead> getBookReads() {
    throw new NotImplementedException();
  }
}
