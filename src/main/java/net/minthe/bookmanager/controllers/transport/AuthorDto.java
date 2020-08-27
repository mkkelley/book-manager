package net.minthe.bookmanager.controllers.transport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.Author;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDto {
  private int id;
  private String name;
  private long createdAt;
  private Long updatedAt;

  public AuthorDto(Author author) {
    id = author.getId();
    name = author.getName();
    createdAt = author.getCreatedAt().toEpochMilli();
    updatedAt = author.getUpdatedAt() != null ? author.getUpdatedAt().toEpochMilli() : null;
  }
}
