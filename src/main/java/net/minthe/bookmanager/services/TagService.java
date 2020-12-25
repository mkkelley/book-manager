package net.minthe.bookmanager.services;

import java.util.List;
import java.util.stream.Collectors;
import net.minthe.bookmanager.models.Tag;
import net.minthe.bookmanager.repositories.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Created by Michael Kelley on 9/10/2020
 */
@Service
public class TagService {

  private final TagRepository tagRepository;

  public TagService(TagRepository tagRepository) {
    this.tagRepository = tagRepository;
  }

  public List<String> searchTags(String search) {
    return tagRepository.getAllByTagContainingIgnoreCaseOrderByCreatedAtDesc(
        search, PageRequest.of(0, 10))
        .stream()
        .map(Tag::getTag)
        .collect(Collectors.toList());
  }
}
