package net.minthe.bookmanager.repositories;

import java.util.List;
import net.minthe.bookmanager.models.Tag;
import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Long> {
  List<Tag> findByTagIn(Iterable<String> tags);
}
