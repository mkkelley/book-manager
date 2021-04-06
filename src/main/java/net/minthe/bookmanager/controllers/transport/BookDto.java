package net.minthe.bookmanager.controllers.transport;

import java.time.Instant;
import java.util.ArrayList;
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
  private String author;
  private Long published;
  private Long createdAt;
  private Long updatedAt;
  private List<BookReadDto> bookReads;
  private List<String> tags;

  public BookDto(Book book) {
    id = book.getId();
    author = book.getAuthor().getName();
    title = book.getTitle();
    published = book.getPublished() != null ? book.getPublished().toEpochMilli() : null;
    createdAt = book.getCreatedAt().toEpochMilli();
    updatedAt = book.getUpdatedAt().map(Instant::toEpochMilli).orElse(null);
    bookReads =
        book.getBookReads() != null
            ? book.getBookReads().stream()
                .sorted(
                    (a, b) ->
                        b.getStarted()
                            .flatMap(first -> a.getStarted().map(first::compareTo))
                            .orElse(-1))
                .map(BookReadDto::new)
                .collect(Collectors.toList())
            : new ArrayList<>();
    tags =
        book.getTags() != null
            ? book.getTags().stream().map(t -> t.getTag().getTag()).collect(Collectors.toList())
            : new ArrayList<>();
  }
}
