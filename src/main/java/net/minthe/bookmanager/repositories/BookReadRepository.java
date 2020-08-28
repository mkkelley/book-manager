package net.minthe.bookmanager.repositories;

import java.util.UUID;
import net.minthe.bookmanager.models.BookRead;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReadRepository extends CrudRepository<BookRead, UUID> {}
