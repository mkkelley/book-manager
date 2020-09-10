package net.minthe.bookmanager.repositories;

import net.minthe.bookmanager.models.BookTag;
import net.minthe.bookmanager.models.BookTagId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookTagRepository extends CrudRepository<BookTag, BookTagId> {

}
