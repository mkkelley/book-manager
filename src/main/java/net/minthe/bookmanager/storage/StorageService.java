package net.minthe.bookmanager.storage;

import java.io.InputStream;
import java.util.List;
import net.minthe.bookmanager.models.BookStorage;

public interface StorageService {

  BookStorage uploadFileForBook(Long bookId, String uploadName, InputStream inputStream);

  List<BookStorage> getUploadsForBook(Long bookId);
}
