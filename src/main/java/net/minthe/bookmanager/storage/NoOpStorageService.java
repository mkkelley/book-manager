package net.minthe.bookmanager.storage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import net.minthe.bookmanager.models.BookStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnMissingBean(
    S3StorageService.class
)
public class NoOpStorageService implements StorageService {

  @Override
  public BookStorage uploadFileForBook(Long bookId, String uploadName, InputStream inputStream) {
    return null;
  }

  @Override
  public List<BookStorage> getUploadsForBook(Long bookId) {
    return new ArrayList<>();
  }
}
