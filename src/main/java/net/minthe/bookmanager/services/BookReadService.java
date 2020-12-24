package net.minthe.bookmanager.services;

import java.time.Instant;
import java.util.UUID;
import net.minthe.bookmanager.controllers.transport.AddBookReadRequest;
import net.minthe.bookmanager.controllers.transport.FinishBookReadRequest;
import net.minthe.bookmanager.models.BookRead;
import net.minthe.bookmanager.repositories.BookReadRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@Service
public class BookReadService {
  private final BookReadRepository bookReadRepository;
  private final AuthService authService;

  public BookReadService(BookReadRepository bookReadRepository, AuthService authService) {
    this.bookReadRepository = bookReadRepository;
    this.authService = authService;
  }

  public BookRead addBookRead(AddBookReadRequest request) {
    var newRead = new BookRead();
    newRead.setAudiobook(request.isAudiobook());
    newRead.setBookId(request.getBookId());
    newRead.setId(request.getId());
    newRead.setUsername(authService.getUsername());
    newRead.setStarted(Instant.ofEpochMilli(request.getStarted()));

    return bookReadRepository.save(newRead);
  }

  @PreAuthorize("@bookReadAuthorizationService.currentUserIsAuthorized(#request.id)")
  public BookRead finishBookRead(FinishBookReadRequest request) {
    var bookReadOpt = bookReadRepository.findById(request.getId());
    if (bookReadOpt.isEmpty()) {
      throw BadRequest.create(
          HttpStatus.BAD_REQUEST, "id does not exist", HttpHeaders.EMPTY, null, null);
    }
    var bookRead = bookReadOpt.get();
    bookRead.setFinished(Instant.ofEpochMilli(request.getFinished()));
    return bookReadRepository.save(bookRead);
  }

  @PreAuthorize("@bookReadAuthorizationService.currentUserIsAuthorized(#id)")
  public BookRead deleteBookRead(UUID id) {
    var bookRead = bookReadRepository.findById(id).orElseThrow();
    bookReadRepository.deleteById(id);
    return bookRead;
  }
}
