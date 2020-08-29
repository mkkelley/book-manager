import {Component, OnInit} from '@angular/core';
import {BookService} from '../../services/book.service';
import {PagedResult} from '../../models/paged-result';
import {Book} from '../../models/book';
import {Observable} from 'rxjs';
import {AddBookRequest} from '../../models/add-book-request';

@Component({
  selector: 'app-book-index',
  templateUrl: './book-index.component.html',
  styleUrls: ['./book-index.component.scss'],
})
export class BookIndexComponent implements OnInit {
  public books$: Observable<PagedResult<Book>>;
  public newBooks: { created: boolean; book: Book }[];

  constructor(private bookService: BookService) {
  }

  ngOnInit(): void {
    this.books$ = this.bookService.getBooks();
    this.newBooks = [];
  }

  newBook(): void {
    this.newBooks.push({created: false, book: null});
  }

  createBook(x: any, request: AddBookRequest) {
    this.bookService.createBook(request).subscribe((book) => {
      const newBook = this.newBooks.find((nb) => nb === x);
      newBook.book = book;
      newBook.created = true;
      this.newBooks = [...this.newBooks];
    });
  }
}
