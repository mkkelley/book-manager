package net.minthe.bookmanager.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PagedResult<T> {
  public int page;
  public int size;
  public List<T> data;
}
