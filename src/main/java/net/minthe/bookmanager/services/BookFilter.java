package net.minthe.bookmanager.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minthe.bookmanager.controllers.transport.BookTitle;

/** Created by Michael Kelley on 9/13/2020 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookFilter {
  private BookTitle search;
  private Boolean unfinished;
  private String tag;
}
