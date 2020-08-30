import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Book } from '../../models/book';
import { AddBookReadRequest } from '../../models/add-book-read-request';
import { FinishBookReadRequest } from '../../models/finish-book-read-request';
import { v4 as uuidv4 } from 'uuid';
import { BookService } from '../../services/book.service';

@Component({
  selector: 'app-book',
  templateUrl: './book.component.html',
  styleUrls: ['./book.component.scss'],
})
export class BookComponent implements OnInit {
  @Input() book: Book;
  @Output() deleteBook = new EventEmitter<number>();

  constructor(private bookService: BookService) {}

  ngOnInit(): void {}

  newBookRead(bookId: number, audiobook: boolean) {
    const request: AddBookReadRequest = {
      audiobook: audiobook,
      id: uuidv4(),
      bookId: bookId,
      started: new Date().getTime(),
    };
    this.bookService.createBookRead(request).subscribe((bookRead) => {
      this.book.bookReads = [...this.book.bookReads, bookRead];
    });
  }

  finishBookRead(id: string, bookId: number) {
    const request: FinishBookReadRequest = {
      finished: new Date().getTime(),
      id: id,
    };
    this.bookService.finishBookRead(bookId, request).subscribe((bookRead) => {
      const r = this.book.bookReads.find((read) => read.id === bookRead.id);
      r.finished = bookRead.finished;
      this.book.bookReads = [...this.book.bookReads];
    });
  }

  deleteBookRead(id: string, bookId: number) {
    this.bookService.deleteBookRead(bookId, id).subscribe(() => {
      this.book.bookReads = this.book.bookReads.filter((br) => br.id !== id);
    });
  }

  delete() {
    this.deleteBook.emit(this.book.id);
  }
}
