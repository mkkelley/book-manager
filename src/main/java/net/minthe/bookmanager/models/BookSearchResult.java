package net.minthe.bookmanager.models;

import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
public class BookSearchResult {
  private Page<Book> books;
  private Collection<BookRead> bookReads;
}
