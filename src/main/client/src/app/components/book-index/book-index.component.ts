import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { PagedResult } from '../../models/paged-result';
import { Book } from '../../models/book';
import { Observable } from 'rxjs';
import { AddBookRequest } from '../../models/add-book-request';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-book-index',
  templateUrl: './book-index.component.html',
  styleUrls: ['./book-index.component.scss'],
})
export class BookIndexComponent implements OnInit {
  public books$: Observable<PagedResult<Book>>;
  public newBooks: { created: boolean; book: Book }[];
  public page = 0;
  public size = 5;

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    route.queryParamMap.subscribe((paramMap) => {
      if (paramMap.has('page')) {
        this.page = +paramMap.get('page');
      }
      if (paramMap.has('size')) {
        this.size = +paramMap.get('size');
      }
      this.changePage();
    });
  }

  ngOnInit(): void {
    this.newBooks = [];
  }

  changePage(): void {
    this.books$ = this.bookService.getBooks(this.page, this.size);
    this.newBooks = [];
    this.router.navigate([], {
      queryParams: {
        page: this.page,
        size: this.size,
      },
      skipLocationChange: true,
    });
  }

  newBook(): void {
    this.newBooks.push({ created: false, book: null });
  }

  createBook(x: any, request: AddBookRequest) {
    this.bookService.createBook(request).subscribe((book) => {
      const newBook = this.newBooks.find((nb) => nb === x);
      newBook.book = book;
      newBook.created = true;
      this.newBooks = [...this.newBooks];
    });
  }

  deleteBook(id: number) {
    this.bookService.deleteBook(id).subscribe(() => {
      this.changePage();
    });
  }

  removeBookForm(newBook) {
    this.newBooks = this.newBooks.filter((x) => x !== newBook);
  }

  nextPage() {
    this.page = this.page + 1;
    this.changePage();
  }

  prevPage() {
    this.page = this.page - 1;
    this.changePage();
  }
}
