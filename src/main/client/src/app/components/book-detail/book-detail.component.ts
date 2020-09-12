import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookDetail } from '../../models/book-detail';
import { BookService } from '../../services/book.service';
import { BookNoteService } from '../../services/book-note.service';
import { BookNote } from '../../models/book-note';
import { BookTagService } from '../../services/book-tag.service';

@Component({
  selector: 'app-book-detail',
  templateUrl: './book-detail.component.html',
  styleUrls: ['./book-detail.component.scss'],
})
export class BookDetailComponent implements OnInit {
  public book: BookDetail;

  constructor(
    private route: ActivatedRoute,
    private bookService: BookService,
    private bookNoteService: BookNoteService,
    private bookTagService: BookTagService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe((data) => {
      this.book = data.book;
    });
  }

  deleteRead(readId: string): void {
    this.bookService.deleteBookRead(this.book.id, readId).subscribe((read) => {
      this.book.bookReads = this.book.bookReads.filter((x) => x.id !== read.id);
    });
  }

  finishRead(readId: string): void {
    this.bookService
      .finishBookRead(this.book.id, {
        finished: new Date().getTime(),
        id: readId,
      })
      .subscribe((read) => {
        const br = this.book.bookReads.find((x) => x.id === read.id);
        br.finished = read.finished;
        this.book.bookReads = [...this.book.bookReads];
      });
  }

  createNote(note: Partial<BookNote>): void {
    this.bookNoteService.createNote(this.book.id, note.notes).subscribe((n) => {
      this.book.notes = [n, ...this.book.notes];
    });
  }

  createTag(tag: string): void {
    this.bookTagService
      .addBookTag(this.book.id, tag)
      .subscribe((book) => (this.book = { ...this.book, tags: book.tags }));
  }

  deleteTag(tag: string): void {
    this.bookTagService
      .removeBookTag(this.book.id, tag)
      .subscribe((book) => (this.book = { ...this.book, tags: book.tags }));
  }
}
