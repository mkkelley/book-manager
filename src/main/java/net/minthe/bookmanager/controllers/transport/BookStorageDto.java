package net.minthe.bookmanager.controllers.transport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.models.BookStorage;

/**
 * Created by Michael Kelley on 9/19/2020
 */
@Getter
@Setter
@NoArgsConstructor
public class BookStorageDto {

  private Long id;
  private Long bookId;
  private String bucket;
  private String storageKey;
  private String filename;

  public BookStorageDto(BookStorage storage) {
    this.id = storage.getId();
    this.bookId = storage.getBookId();
    this.bucket = storage.getBucket();
    this.storageKey = storage.getStorageKey();
    this.filename = storage.getFilename();
  }
}
