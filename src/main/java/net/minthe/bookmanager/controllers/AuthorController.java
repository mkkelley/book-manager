package net.minthe.bookmanager.controllers;

import java.util.List;
import net.minthe.bookmanager.models.Author;
import net.minthe.bookmanager.repositories.AuthorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {
  private final AuthorRepository authorRepository;

  public AuthorController(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @GetMapping("/typeahead")
  public List<Author> getAuthorTypeahead(@RequestParam String author) {
    return authorRepository.findByNameContainingIgnoreCaseOrderByName(author);
  }
}
