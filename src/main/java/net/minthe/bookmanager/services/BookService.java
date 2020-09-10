package net.minthe.bookmanager.services;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Collectors;
import net.minthe.bookmanager.controllers.transport.AddBookRequest;
import net.minthe.bookmanager.models.Author;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.models.BookTag;
import net.minthe.bookmanager.models.Tag;
import net.minthe.bookmanager.repositories.AuthorRepository;
import net.minthe.bookmanager.repositories.BookRepository;
import net.minthe.bookmanager.repositories.BookTagRepository;
import net.minthe.bookmanager.repositories.TagRepository;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  private final BookRepository bookRepository;
  private final AuthorRepository authorRepository;
  private final TagRepository tagRepository;
  private final BookTagRepository bookTagRepository;

  public BookService(
      BookRepository bookRepository,
      AuthorRepository authorRepository,
      TagRepository tagRepository,
      BookTagRepository bookTagRepository) {
    this.bookRepository = bookRepository;
    this.authorRepository = authorRepository;
    this.tagRepository = tagRepository;
    this.bookTagRepository = bookTagRepository;
  }

  public Book getBook(Long id) {
    return this.bookRepository.findById(id).orElseThrow();
  }

  public Page<Book> getBooks(Pageable pageable) {
    return bookRepository.getBooksByOrderByCreatedAtDesc(pageable);
  }

  public Page<Book> searchBooks(String search, Pageable pageable) {
    return bookRepository.getBooksByTitleContainingIgnoreCaseOrderByCreatedAtDesc(search, pageable);
  }

  public Book addBook(AddBookRequest request) {
    var authors = authorRepository.findByNameOrderById(request.getAuthorName());
    Author author;
    if (authors.size() == 0) {
      author = authorRepository.save(new Author(null, request.getAuthorName()));
    } else {
      author = authors.get(0);
    }

    var book = new Book();
    book.setAuthorId(author.getId());
    book.setTitle(request.getTitle());
    book.setPublished(request.getPublished().map(Instant::ofEpochMilli).orElse(null));
    bookRepository.save(book);

    return bookRepository.findById(book.getId()).orElseThrow();
  }

  public Book deleteBook(Long id) {
    var book = bookRepository.findById(id).orElseThrow();
    Hibernate.initialize(book.getAuthor());
    Hibernate.initialize(book.getBookReads());
    bookRepository.deleteById(id);
    return book;
  }

  public Book addTags(Long bookId, Collection<String> tags) {
    var book = bookRepository.findById(bookId).orElseThrow();
    var presentTags =
        book.getTags().stream()
            .map(t -> t.getTag().getTag())
            .collect(Collectors.toUnmodifiableList());
    var newBookTags =
        tags.stream()
            .filter(t -> !presentTags.contains(t))
            .collect(Collectors.toUnmodifiableList());
    var existingTags =
        tagRepository.findByTagIn(newBookTags).stream().collect(Collectors.toUnmodifiableList());
    var existingTagNames =
        existingTags.stream().map(Tag::getTag).collect(Collectors.toUnmodifiableList());
    var newTags =
        newBookTags.stream()
            .filter(nbt -> !existingTagNames.contains(nbt))
            .map(
                newTagTag -> {
                  var newTag = new Tag();
                  newTag.setId(null);
                  newTag.setTag(newTagTag);
                  return tagRepository.save(newTag);
                })
            .map(
                tag -> {
                  var bt = new BookTag();
                  bt.setTagId(tag.getId());
                  bt.setBookId(bookId);
                  return bookTagRepository.save(bt);
                })
            .collect(Collectors.toUnmodifiableList());

    book.getTags().addAll(newTags);
    book.getTags()
        .addAll(
            existingTags.stream()
                .map(
                    t -> {
                      var bt = new BookTag();
                      bt.setTagId(t.getId());
                      bt.setBookId(bookId);
                      return bookTagRepository.save(bt);
                    })
                .collect(Collectors.toList()));
    return bookRepository.save(book);
  }

  public Book removeTag(Long bookId, String tag) {
    var book = bookRepository.findById(bookId).orElseThrow();
    book.getTags().removeIf(t -> t.getTag().getTag().equals(tag));
    return bookRepository.save(book);
  }
}
