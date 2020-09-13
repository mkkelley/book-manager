package net.minthe.bookmanager.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Created by Michael Kelley on 9/13/2020 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookFilter {
  private String search;
  private Boolean unfinished;
  private String tag;
}
