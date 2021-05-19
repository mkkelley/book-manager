package net.minthe.bookmanager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Random;
import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.CreateBookNoteRequest;
import net.minthe.bookmanager.models.BookNote;
import net.minthe.bookmanager.repositories.BookNoteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by Michael Kelley on 9/6/2020
 */
public class BookNoteServiceTest {

  @Mock
  private BookNoteRepository bookNoteRepository;
  @Mock
  private AuthService authService;

  private CreateBookNoteRequest createBookNoteRequest;
  private BookNoteService service;
  private AutoCloseable mockClose;

  @BeforeEach
  public void setup() {
    mockClose = MockitoAnnotations.openMocks(this);
    service = new BookNoteService(bookNoteRepository, authService);
    Random random = new Random();
    createBookNoteRequest = new CreateBookNoteRequest();
    createBookNoteRequest.setBookId(random.nextLong());
    createBookNoteRequest.setId(null);
    createBookNoteRequest.setNotes("asdfasdf");
    createBookNoteRequest.setUsername("username");

    given(authService.isAdmin()).willReturn(false);
    given(authService.getUsername()).willReturn("username");

    given(bookNoteRepository.save(any(BookNote.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
  }

  @AfterEach
  public void tearDown() throws Exception {
    mockClose.close();
  }

  @Test
  public void assignsUuid_whenNull() {
    var note = service.createBookNote(createBookNoteRequest);
    assertNotNull(note);
    assertNotNull(note.getId());
  }

  @Test
  public void doesNotAssignUuid_whenNotNull() {
    var uuid = UUID.randomUUID();
    createBookNoteRequest.setId(uuid);
    var note = service.createBookNote(createBookNoteRequest);
    assertNotNull(note);
    assertEquals(uuid, note.getId());
  }

  @Test
  public void assignsUsername_whenNull() {
    createBookNoteRequest.setUsername(null);
    var note = service.createBookNote(createBookNoteRequest);
    assertNotNull(note.getUsername());
    assertEquals(authService.getUsername(), note.getUsername());
  }

  @Test
  public void request_canSetUsername() {
    createBookNoteRequest.setUsername("other_username");
    var note = service.createBookNote(createBookNoteRequest);
    assertNotNull(note.getUsername());
    assertEquals(createBookNoteRequest.getUsername(), note.getUsername());
  }
}
