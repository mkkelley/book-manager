package net.minthe.bookmanager.repositories;

import java.util.List;
import net.minthe.bookmanager.models.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Long> {
  List<Tag> findByTagIn(Iterable<String> tags);

  List<Tag> getAllByTagContainingIgnoreCaseOrderByCreatedAtDesc(String tag);
}
