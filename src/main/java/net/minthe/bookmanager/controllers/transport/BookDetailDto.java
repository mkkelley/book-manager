package net.minthe.bookmanager.controllers.transport;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.models.BookRead;

/** Created by Michael Kelley on 8/29/2020 */
@Getter
@Setter
@NoArgsConstructor
public class BookDetailDto extends BookDto {
  private List<BookNoteDto> notes;

  public BookDetailDto(Book book, Collection<BookRead> bookReads) {
    super(book, bookReads);
    this.notes = book.getBookNotes().stream().map(BookNoteDto::new).collect(Collectors.toList());
  }
}
