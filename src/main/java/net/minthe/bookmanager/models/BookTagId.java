package net.minthe.bookmanager.models;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BookTagId implements Serializable {

  @Column(name = "book_id")
  private Long bookId;

  @Column(name = "tag_id")
  private Long tagId;
}
