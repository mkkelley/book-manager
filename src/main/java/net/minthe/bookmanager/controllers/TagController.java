package net.minthe.bookmanager.controllers;

import java.util.List;
import net.minthe.bookmanager.services.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Created by Michael Kelley on 9/10/2020 */
@RestController
@RequestMapping("/api/tags")
public class TagController {
  private final TagService tagService;

  public TagController(TagService tagService) {
    this.tagService = tagService;
  }

  @GetMapping("typeahead")
  public List<String> searchTags(@RequestParam String tag) {
    return this.tagService.searchTags(tag);
  }
}
