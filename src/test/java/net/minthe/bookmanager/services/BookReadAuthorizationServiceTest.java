package net.minthe.bookmanager.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.Optional;
import java.util.UUID;
import net.minthe.bookmanager.models.BookRead;
import net.minthe.bookmanager.repositories.BookReadRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BookReadAuthorizationServiceTest {

  private BookReadAuthorizationService bookReadAuthorizationService;
  @Mock
  private BookReadRepository bookReadRepository;
  @Mock
  private AuthService authService;
  private UUID uuid;
  private AutoCloseable mockClose;

  @BeforeEach
  public void setup() {
    mockClose = MockitoAnnotations.openMocks(this);
    bookReadAuthorizationService = new BookReadAuthorizationService(bookReadRepository,
        authService);
    uuid = UUID.randomUUID();

    var read = new BookRead();
    read.setUsername("username");
    read.setId(uuid);

    given(authService.getUsername()).willReturn("username");
    given(authService.isAdmin()).willReturn(false);
    given(bookReadRepository.findById(any())).willReturn(java.util.Optional.of(read));
  }

  @AfterEach
  public void tearDown() throws Exception {
    mockClose.close();
  }

  @Test
  public void willNotAuthorize_forNoReadFound() {
    given(bookReadRepository.findById(any())).willReturn(Optional.empty());
    var authorized = bookReadAuthorizationService.currentUserIsAuthorized(uuid);
    assertFalse(authorized);
  }

  @Test
  public void willAuthorize_forMatchingUsername() {
    var authorized = bookReadAuthorizationService.currentUserIsAuthorized(uuid);
    assertTrue(authorized);
  }

  @Test
  public void willNotAuthorize_forOtherUsername() {
    given(authService.getUsername()).willReturn("other_username");
    var authorized = bookReadAuthorizationService.currentUserIsAuthorized(uuid);
    assertFalse(authorized);
  }

  @Test
  public void willAuthorize_forAdmin() {
    given(authService.isAdmin()).willReturn(true);
    var authorized = bookReadAuthorizationService.currentUserIsAuthorized(uuid);
    assertTrue(authorized);
  }

  @Test
  public void willAuthorize_forOtherAdmin() {
    given(authService.getUsername()).willReturn("other_admin");
    given(authService.isAdmin()).willReturn(true);
    var authorized = bookReadAuthorizationService.currentUserIsAuthorized(uuid);
    assertTrue(authorized);
  }
}
