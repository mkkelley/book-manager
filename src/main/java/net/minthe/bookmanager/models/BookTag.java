package net.minthe.bookmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@IdClass(BookTagId.class)
@Table(name = "books_tags")
@Getter
@Setter
public class BookTag extends Auditable {
  @JoinColumn(name = "book_id", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Book book;

  @Id
  @Column(name = "book_id", nullable = false)
  private Long bookId;

  @JoinColumn(name = "tag_id", insertable = false, updatable = false)
  @ManyToOne(optional = false)
  private Tag tag;

  @Id
  @Column(name = "tag_id", nullable = false)
  private Long tagId;
}
