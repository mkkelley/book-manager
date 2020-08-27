package net.minthe.bookmanager.models;

import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "authors")
public class Author {
  @Id private int id;
  private String name;
  private Instant createdAt;
  private Instant updatedAt;
}
