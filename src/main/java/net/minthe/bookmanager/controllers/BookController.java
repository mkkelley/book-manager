package net.minthe.bookmanager.controllers;

import java.util.ArrayList;
import java.util.List;
import net.minthe.bookmanager.models.Book;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/books")
public class BookController {

  @GetMapping() public List<Book> getBooks() {
    return new ArrayList<>();
  }
}
