package net.minthe.bookmanager.repositories;

import java.util.UUID;
import net.minthe.bookmanager.models.BookNote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/** Created by Michael Kelley on 8/29/2020 */
@Repository
public interface BookNoteRepository extends CrudRepository<BookNote, UUID> {}
