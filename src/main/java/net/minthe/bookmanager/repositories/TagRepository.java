package net.minthe.bookmanager.repositories;

import java.util.List;
import net.minthe.bookmanager.models.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
  List<Tag> findByTagIn(Iterable<String> tags);

  @Query(
      value =
          "SELECT t.id, t.tag, t.created_at, t.created_by, t.updated_at, t.updated_by "
              + "FROM tags_ordered t "
              + "WHERE t.tag ILIKE '%' || ?1 || '%' "
              + "LIMIT 10",
      nativeQuery = true)
  List<Tag> getAllByTagContainingIgnoreCaseOrderByCount(String tag);
}
