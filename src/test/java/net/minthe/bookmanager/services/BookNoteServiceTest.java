package net.minthe.bookmanager.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Random;
import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.CreateBookNoteRequest;
import net.minthe.bookmanager.models.BookNote;
import net.minthe.bookmanager.models.User;
import net.minthe.bookmanager.repositories.BookNoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/** Created by Michael Kelley on 9/6/2020 */
public class BookNoteServiceTest {
  @Mock private BookNoteRepository bookNoteRepository;
  @Mock private AuthService authService;

  private CreateBookNoteRequest createBookNoteRequest;
  private BookNoteService service;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    service = new BookNoteService(bookNoteRepository, authService);
    Random random = new Random();
    createBookNoteRequest = new CreateBookNoteRequest();
    createBookNoteRequest.setBookId(random.nextLong());
    createBookNoteRequest.setId(null);
    createBookNoteRequest.setNotes("asdfasdf");
    createBookNoteRequest.setUsername("username");

    given(authService.isAdmin()).willReturn(false);
    given(authService.getUsername()).willReturn("username");
    User user = new User();
    user.setUsername("user_username");
    user.setEnabled(true);
    var auth = new UsernamePasswordAuthenticationToken(user, null);
    SecurityContextHolder.getContext().setAuthentication(auth);

    given(bookNoteRepository.save(any(BookNote.class)))
        .willAnswer(invocation -> invocation.getArgument(0));
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
  public void user_canOnlyCreatePersonalNotes() {
    createBookNoteRequest.setUsername("other_username");
    var note = service.createBookNote(createBookNoteRequest);
    assertNotNull(note.getUsername());
    assertEquals(authService.getUsername(), note.getUsername());
  }

  @Test
  public void admin_canCreateNotesForOthers() {
    given(authService.isAdmin()).willReturn(true);
    createBookNoteRequest.setUsername("other_username");
    var note = service.createBookNote(createBookNoteRequest);
    assertNotNull(note.getUsername());
    assertNotEquals(authService.getUsername(), note.getUsername());
    assertEquals(createBookNoteRequest.getUsername(), note.getUsername());
  }
}
