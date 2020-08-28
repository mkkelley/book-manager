import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { PagedResult } from '../../models/paged-result';
import { Book } from '../../models/book';
import { Observable } from 'rxjs';
import { AddBookRequest } from '../../models/add-book-request';
import { AddBookReadRequest } from '../../models/add-book-read-request';
import { v4 as uuidv4 } from 'uuid';
import { FinishBookReadRequest } from '../../models/finish-book-read-request';

@Component({
  selector: 'app-book-index',
  templateUrl: './book-index.component.html',
  styleUrls: ['./book-index.component.scss'],
})
export class BookIndexComponent implements OnInit {
  public books$: Observable<PagedResult<Book>>;
  public newBooks: {}[];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.books$ = this.bookService.getBooks();
    this.newBooks = [];
  }

  newBook(): void {
    this.newBooks.push({});
  }

  createBook(x: any, request: AddBookRequest) {
    this.bookService.createBook(request).subscribe();
    this.newBooks = this.newBooks.filter((nb) => nb !== x);
  }

  newBookRead(bookId: number, audiobook: boolean) {
    const request: AddBookReadRequest = {
      audiobook: audiobook,
      id: uuidv4(),
      bookId: bookId,
      started: new Date().getTime(),
    };
    this.bookService.createBookRead(request).subscribe();
  }

  finishBookRead(id: string, bookId: number) {
    const request: FinishBookReadRequest = {
      finished: new Date().getTime(),
      id: id,
    };
    this.bookService.finishBookRead(bookId, request).subscribe();
  }

  deleteBookRead(id: string, bookId: number) {
    this.bookService.deleteBookRead(bookId, id).subscribe();
  }
}
