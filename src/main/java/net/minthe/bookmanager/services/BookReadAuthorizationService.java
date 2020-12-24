package net.minthe.bookmanager.services;

import java.util.UUID;
import net.minthe.bookmanager.repositories.BookReadRepository;
import org.springframework.stereotype.Service;

@Service
public class BookReadAuthorizationService {

  private final BookReadRepository bookReadRepository;
  private final AuthService authService;

  public BookReadAuthorizationService(
      BookReadRepository bookReadRepository,
      AuthService authService) {
    this.bookReadRepository = bookReadRepository;
    this.authService = authService;
  }

  public boolean currentUserIsAuthorized(UUID bookReadGuid) {
    return authService.isAdmin()
        || bookReadRepository.findById(bookReadGuid)
        .map(bookRead -> bookRead.getUsername().equals(authService.getUsername()))
        .orElse(false);
  }
}
