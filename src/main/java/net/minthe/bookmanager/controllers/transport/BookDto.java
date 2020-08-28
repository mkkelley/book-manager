package net.minthe.bookmanager.controllers.transport;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.Book;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
  private Long id;
  private String title;
  private AuthorDto author;
  private Long published;
  private Long createdAt;
  private Long updatedAt;
  private List<BookReadDto> bookReads;

  public BookDto(Book book) {
    id = book.getId();
    author = new AuthorDto(book.getAuthor());
    title = book.getTitle();
    published = book.getPublished() != null ? book.getPublished().toEpochMilli() : null;
    createdAt = book.getCreatedAt().toEpochMilli();
    updatedAt = book.getUpdatedAt().map(Instant::toEpochMilli).orElse(null);
    bookReads =
        book.getBookReads().stream()
            .sorted(
                (a, b) ->
                    a.getStarted()
                        .flatMap(first -> b.getStarted().map(first::compareTo))
                        .orElse(-1))
            .map(BookReadDto::new)
            .collect(Collectors.toList());
  }
}
