import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { BookDetail } from '../../models/book-detail';
import { BookService } from '../../services/book.service';
import { BookNoteService } from '../../services/book-note.service';
import { BookNote } from '../../models/book-note';
import { BookTagService } from '../../services/book-tag.service';
import { Location } from '@angular/common';
import { UpdateBookRequest } from '../../models/update-book-request';
import { BookStorageService } from '../../services/book-storage.service';
import { Observable } from 'rxjs';
import { BookStorage } from '../../models/book-storage';

@Component({
  selector: 'app-book-detail',
  templateUrl: './book-detail.component.html',
  styleUrls: ['./book-detail.component.scss'],
})
export class BookDetailComponent implements OnInit {
  public book: BookDetail;
  public deleteMode: boolean;
  public bookUploads$: Observable<BookStorage[]>;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private bookService: BookService,
    private bookNoteService: BookNoteService,
    private bookTagService: BookTagService,
    private bookStorageService: BookStorageService
  ) {}

  ngOnInit(): void {
    this.deleteMode = false;
    this.route.data.subscribe((data) => {
      this.book = data.book;
      this.bookUploads$ = this.bookStorageService.getFiles(this.book.id);
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
      this.book.notes = [...this.book.notes, n];
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

  delete(): void {
    this.bookService.deleteBook(this.book.id).subscribe(() => {
      this.location.back();
    });
  }

  public updateTitle(title: string): void {
    const request = new UpdateBookRequest();
    request.id = this.book.id;
    request.authorName = this.book.author;
    request.published = this.book.published;
    request.title = title;
    this.bookService
      .updateBook(request)
      .subscribe((book) => (this.book = { ...this.book, ...book }));
  }

  public updateAuthor(author: string): void {
    const request = new UpdateBookRequest();
    request.id = this.book.id;
    request.authorName = author;
    request.published = this.book.published;
    request.title = this.book.title;
    this.bookService
      .updateBook(request)
      .subscribe((book) => (this.book = { ...this.book, ...book }));
  }

  upload(value: File): void {
    this.bookStorageService.uploadFile(value, this.book.id).subscribe(() => {
      this.bookUploads$ = this.bookStorageService.getFiles(this.book.id);
    });
  }
}
