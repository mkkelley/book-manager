package net.minthe.bookmanager.models;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class Tag extends Auditable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String tag;

  @ManyToMany
  @JoinTable(
      name = "books_tags",
      joinColumns = {@JoinColumn(name = "tag_id")},
      inverseJoinColumns = {@JoinColumn(name = "book_id")})
  @BatchSize(size = 20)
  private List<Book> books;
}
