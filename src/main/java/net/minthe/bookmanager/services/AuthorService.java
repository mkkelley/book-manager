package net.minthe.bookmanager.services;

import java.util.ArrayList;
import java.util.stream.Stream;
import net.minthe.bookmanager.models.Author;
import net.minthe.bookmanager.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public Stream<Author> getAuthorTypeahead(String authorName) {
    var authors = authorRepository.findByNameCloseToIgnoreCaseOrderByCloseness(authorName);
    return authors.stream().filter(a -> a.getBooks().size() != 0);
  }

  public Author getOrCreate(String name) {
    var authors = authorRepository.findByNameOrderById(name);
    if (authors.size() == 0) {
      return authorRepository.save(new Author(null, name, new ArrayList<>()));
    } else {
      return authors.get(0);
    }
  }
}
