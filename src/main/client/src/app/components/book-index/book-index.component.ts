import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { PagedResult } from '../../models/paged-result';
import { Book } from '../../models/book';
import { Subject } from 'rxjs';
import { AddBookRequest } from '../../models/add-book-request';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { debounceTime, distinctUntilChanged, takeUntil } from 'rxjs/operators';
import { DEFAULT_PAGE_SIZE } from '../../app.constants';

@Component({
  selector: 'app-book-index',
  templateUrl: './book-index.component.html',
  styleUrls: ['./book-index.component.scss'],
})
export class BookIndexComponent implements OnInit {
  public books: PagedResult<Book>;
  public newBooks: { created: boolean; book: Book }[];
  public page = 0;
  public size = DEFAULT_PAGE_SIZE;
  public tag: string;
  public unfinished: boolean;
  public searchControl = new FormControl('');
  public unfinishedControl = new FormControl(false);

  private destroy$ = new Subject();
  /**
   * When the user is searching, there can be a bad interaction with the debounce when the
   * user types a character after the debounce time and before the request and navigation
   * finishes.
   *
   * If this value is non-null, the user has executed a search. If the value changes between the
   * time that the search starts and the time that we update the box with the value from navigation,
   * the user has kept typing and we should not update the search box.
   */
  private lastSearch: string = null;

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.route.data.subscribe((data) => (this.books = data.books));
    route.queryParamMap.subscribe((paramMap) => {
      this.page = +paramMap.get('page');
      this.size =
        paramMap.get('size') != null
          ? +paramMap.get('size')
          : DEFAULT_PAGE_SIZE;
      this.tag = paramMap.get('tag');
      if (
        paramMap.get('search') !== this.searchControl.value &&
        (this.lastSearch == null ||
          this.lastSearch === this.searchControl.value)
      ) {
        this.searchControl.setValue(paramMap.get('search'));
      }
      if (JSON.parse(paramMap.get('unfinished')) !== this.unfinished) {
        this.unfinishedControl.setValue(JSON.parse(paramMap.get('unfinished')));
      }
      this.newBooks = this.newBooks?.filter((nb) => nb.created === false);
    });
  }

  ngOnInit(): void {
    this.newBooks = [];
    this.searchControl.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged(), takeUntil(this.destroy$))
      .subscribe((value) => {
        this.lastSearch = this.searchControl.value;
        return this.router.navigate([], {
          relativeTo: this.route,
          queryParamsHandling: 'merge',
          queryParams: {
            search: value,
          },
        });
      });
    this.unfinishedControl.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe((value) =>
        this.router.navigate([], {
          relativeTo: this.route,
          queryParamsHandling: 'merge',
          queryParams: {
            unfinished: value,
          },
        })
      );
  }

  newBook(): void {
    this.newBooks = [{ created: false, book: null }, ...this.newBooks];
  }

  createBook(x: any, request: AddBookRequest): void {
    this.bookService.createBook(request).subscribe((book) => {
      const newBook = this.newBooks.find((nb) => nb === x);
      newBook.book = book;
      newBook.created = true;
      this.newBooks = [...this.newBooks];
    });
  }

  removeBookForm(newBook): void {
    this.newBooks = this.newBooks.filter((x) => x !== newBook);
  }

  pageChange(page: number): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParamsHandling: 'merge',
      queryParams: {
        page: page,
      },
    });
  }

  deleteNewBook(bookId: number): void {
    this.bookService.deleteBook(bookId).subscribe(() => {
      this.newBooks = this.newBooks.filter((x) => x.book.id !== bookId);
    });
  }

  removeTagSearch(): void {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParamsHandling: 'merge',
      queryParams: {
        tag: null,
      },
    });
  }
}
