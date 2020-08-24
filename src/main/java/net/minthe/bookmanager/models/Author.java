package net.minthe.bookmanager.models;

import java.time.Instant;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Author {
  @Id public int id;
  public String name;
  public Instant createdAt;
  public Instant updatedAt;

  @OneToMany public List<Book> books;
}
