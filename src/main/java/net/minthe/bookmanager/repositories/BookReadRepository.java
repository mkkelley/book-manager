package net.minthe.bookmanager.repositories;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minthe.bookmanager.models.BookRead;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReadRepository extends CrudRepository<BookRead, UUID> {
  List<BookRead> getBookReadsByUsernameAndBookIdInOrderByCreatedAtDesc(String username, Collection<Long> bookIds);
  List<BookRead> getBookReadsByUsernameAndBookIdOrderByCreatedAtDesc(String username, Long bookId);
}
