package net.minthe.bookmanager.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import net.minthe.bookmanager.models.Author;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.repositories.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class AuthorServiceTest {

  @Mock private AuthorRepository authorRepository;

  private AuthorService authorService;
  private AutoCloseable mockClose;

  @BeforeEach
  public void setup() {
    mockClose = MockitoAnnotations.openMocks(this);
    authorService = new AuthorService(authorRepository);
  }

  @AfterEach
  public void tearDown() throws Exception {
    mockClose.close();
  }

  @Test
  public void getOrCreate_creates() {
    given(authorRepository.findByNameOrderById(any(String.class))).willReturn(List.of());

    authorService.getOrCreate("hello");

    verify(authorRepository, times(1))
        .save(argThat(argument -> argument.getName().equals("hello")));
  }

  @Test
  public void getOrCreate_gets() {
    given(authorRepository.findByNameOrderById(any(String.class)))
        .willReturn(List.of(new Author(123L, "hello", List.of())));

    authorService.getOrCreate("hello");

    verify(authorRepository, times(0))
        .save(any(Author.class));
  }

  @Test
  public void typeahead_ignoresAuthorsWithNoBooks() {
    given(authorRepository.findByNameCloseToIgnoreCaseOrderByCloseness(any(String.class)))
        .willReturn(List.of(new Author(
            123L, "hello", List.of()
        )));

    var typeahead = authorService.getAuthorTypeahead("hello");
    assertEquals(0, typeahead.count());
  }

  @Test
  public void typeahead_returnsAuthorsWithBooks() {
    given(authorRepository.findByNameCloseToIgnoreCaseOrderByCloseness(any(String.class)))
        .willReturn(List.of(new Author(
            123L, "hello", List.of(new Book())
        )));

    var typeahead = authorService.getAuthorTypeahead("hello");
    assertEquals(1, typeahead.count());
  }
}
