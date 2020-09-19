package net.minthe.bookmanager.repositories;

import java.util.List;
import net.minthe.bookmanager.models.BookStorage;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Michael Kelley on 9/19/2020
 */
public interface BookStorageRepository extends CrudRepository<BookStorage, Long> {

  List<BookStorage> findByBookId(Long bookId);
}
