package net.minthe.bookmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by Michael Kelley on 9/19/2020
 */
@Entity
@Table(name = "book_storage")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookStorage extends Auditable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "book_id")
  private Long bookId;

  @ManyToOne
  @JoinColumn(name = "book_id", updatable = false, insertable = false)
  private Book book;

  private String bucket;
  private String storageKey;
  private String filename;
}
