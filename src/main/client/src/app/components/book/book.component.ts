import { Component, Input, OnInit } from '@angular/core';
import { Book } from '../../models/book';
import { AddBookReadRequest } from '../../models/add-book-read-request';
import { FinishBookReadRequest } from '../../models/finish-book-read-request';
import { v4 as uuidv4 } from 'uuid';
import { BookService } from '../../services/book.service';

@Component({
  selector: '[app-book]',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
})
export class BookComponent implements OnInit {
  @Input() book: Book;

  constructor(private bookService: BookService) {}

  ngOnInit(): void {}

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
