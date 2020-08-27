package net.minthe.bookmanager.repositories;

import java.util.List;
import net.minthe.bookmanager.models.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {
  List<Author> findByNameOrderById(String name);
}
