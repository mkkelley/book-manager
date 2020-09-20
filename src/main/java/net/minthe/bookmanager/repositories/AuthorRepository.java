package net.minthe.bookmanager.repositories;

import java.util.List;
import net.minthe.bookmanager.models.Author;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, Long> {

  List<Author> findByNameOrderById(String name);

  @Query(
      value =
          "SELECT *, levenshtein(lower(name), lower(:name), 1, 0, 1) l"
              + " FROM authors"
              + " ORDER BY l LIMIT 10",
      nativeQuery = true)
  List<Author> findByNameCloseToIgnoreCaseOrderByCloseness(String name);
}
