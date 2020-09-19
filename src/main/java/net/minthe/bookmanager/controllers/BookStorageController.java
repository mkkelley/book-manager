package net.minthe.bookmanager.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import net.minthe.bookmanager.controllers.transport.BookStorageDto;
import net.minthe.bookmanager.storage.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Michael Kelley on 9/19/2020
 */
@RestController
@RequestMapping("/api/storage")
public class BookStorageController {

  private final StorageService storageService;

  public BookStorageController(StorageService storageService) {
    this.storageService = storageService;
  }

  @PostMapping("books/{bookId}")
  public ResponseEntity<?> uploadFileForBook(
      @PathVariable @NotNull Long bookId, @RequestParam("file") MultipartFile file)
      throws IOException {
    if (!file.getOriginalFilename().strip().matches(".*\\.epub")) {
      return ResponseEntity.badRequest().body("only epub");
    }

    var storage =
        storageService.uploadFileForBook(bookId, file.getOriginalFilename(), file.getInputStream());
    return ResponseEntity.ok(new BookStorageDto(storage));
  }

  @GetMapping("books/{bookId}")
  public List<BookStorageDto> getFilesForBook(@PathVariable @NotNull Long bookId) {
    return storageService.getUploadsForBook(bookId).stream()
        .map(BookStorageDto::new)
        .collect(Collectors.toList());
  }
}
