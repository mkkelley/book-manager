package net.minthe.bookmanager.services;

import java.util.Collection;
import java.util.stream.Collectors;
import net.minthe.bookmanager.models.Book;
import net.minthe.bookmanager.models.BookTag;
import net.minthe.bookmanager.models.Tag;
import net.minthe.bookmanager.repositories.BookRepository;
import net.minthe.bookmanager.repositories.BookTagRepository;
import net.minthe.bookmanager.repositories.TagRepository;
import org.springframework.stereotype.Service;

@Service
public class BookTagService {
  private final BookRepository bookRepository;
  private final BookTagRepository bookTagRepository;
  private final TagRepository tagRepository;

  public BookTagService(
      BookRepository bookRepository,
      BookTagRepository bookTagRepository,
      TagRepository tagRepository) {
    this.bookRepository = bookRepository;
    this.bookTagRepository = bookTagRepository;
    this.tagRepository = tagRepository;
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
                  bt.setTag(tag);
                  bt.setBookId(bookId);
                  bt.setBook(book);
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
                      bt.setTag(t);
                      bt.setBookId(bookId);
                      bt.setBook(book);
                      return bookTagRepository.save(bt);
                    })
                .collect(Collectors.toList()));
    return bookRepository.save(book);
  }

  public Book removeTag(Long bookId, String tag) {
    var book = bookRepository.findById(bookId).orElseThrow();
    var tagToRemove =
        book.getTags().stream()
            .filter(t -> t.getTag().getTag().equals(tag))
            .findFirst()
            .orElseThrow();
    bookTagRepository.delete(tagToRemove);
    book.getTags().remove(tagToRemove);
    return bookRepository.save(book);
  }
}
