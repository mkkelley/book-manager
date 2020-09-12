package net.minthe.bookmanager.controllers;

import java.util.List;
import java.util.stream.Collectors;
import net.minthe.bookmanager.controllers.transport.AuthorDto;
import net.minthe.bookmanager.services.AuthorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

  private final AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @GetMapping("/typeahead")
  public List<AuthorDto> getAuthorTypeahead(@RequestParam String author) {
    return authorService
        .getAuthorTypeahead(author)
        .map(AuthorDto::new)
        .collect(Collectors.toList());
  }
}
