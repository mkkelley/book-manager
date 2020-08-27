package net.minthe.bookmanager.controllers.transport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.Book;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
  private int id;
  private String title;
  private AuthorDto author;
  private Long published;
  private long createdAt;
  private Long updatedAt;

  public BookDto(Book book) {
    id = book.getId();
    author = new AuthorDto(book.getAuthor());
    title = book.getTitle();
    published = book.getPublished() != null ? book.getPublished().toEpochMilli() : null;
    createdAt = book.getCreatedAt().toEpochMilli();
    updatedAt = book.getUpdatedAt() != null ? book.getUpdatedAt().toEpochMilli() : null;
  }
}
