import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { PagedResult } from '../../models/paged-result';
import { Book } from '../../models/book';
import { Observable, Subject } from 'rxjs';
import { AddBookRequest } from '../../models/add-book-request';
import { ActivatedRoute, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { debounceTime, takeUntil } from 'rxjs/operators';
import { DEFAULT_PAGE_SIZE } from '../../app.constants';

@Component({
  selector: 'app-book-index',
  templateUrl: './book-index.component.html',
  styleUrls: ['./book-index.component.scss'],
})
export class BookIndexComponent implements OnInit {
  public books$: Observable<PagedResult<Book>>;
  public newBooks: { created: boolean; book: Book }[];
  public page = 0;
  public size = DEFAULT_PAGE_SIZE;
  public tag: string;
  public unfinished: boolean;
  public searchControl = new FormControl('');
  public unfinishedControl = new FormControl(false);

  private destroy$ = new Subject();

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    route.queryParamMap.subscribe((paramMap) => {
      this.page = +paramMap.get('page');
      this.size =
        paramMap.get('size') != null
          ? +paramMap.get('size')
          : DEFAULT_PAGE_SIZE;
      this.tag = paramMap.get('tag');
      if (paramMap.get('search') !== this.searchControl.value) {
        this.searchControl.setValue(paramMap.get('search'));
      }
      if (JSON.parse(paramMap.get('unfinished')) !== this.unfinished) {
        this.unfinishedControl.setValue(JSON.parse(paramMap.get('unfinished')));
      }
      this.changePage();
    });
  }

  ngOnInit(): void {
    this.newBooks = [];
    this.searchControl.valueChanges
      .pipe(debounceTime(300), takeUntil(this.destroy$))
      .subscribe((value) =>
        this.router.navigate([], {
          relativeTo: this.route,
          queryParamsHandling: 'merge',
          queryParams: {
            search: value,
          },
        })
      );
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

  changePage(): void {
    this.newBooks = [];
    let queryParams: {} = {
      page: this.page,
      size: this.size,
    };
    const search = this.searchControl.value;
    const tag = this.tag;
    const unfinished = this.unfinishedControl.value;
    this.books$ = this.bookService.searchBooks(
      search,
      tag,
      unfinished,
      this.page,
      this.size
    );
    queryParams = {
      ...queryParams,
      search: search != null && search !== '' ? search : null,
      tag: tag != null && tag !== '' ? tag : null,
      unfinished: unfinished != null ? unfinished : null,
    };
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }

  newBook(): void {
    this.newBooks.push({ created: false, book: null });
  }

  createBook(x: any, request: AddBookRequest): void {
    this.bookService.createBook(request).subscribe((book) => {
      const newBook = this.newBooks.find((nb) => nb === x);
      newBook.book = book;
      newBook.created = true;
      this.newBooks = [...this.newBooks];
    });
  }

  deleteBook(id: number): void {
    this.bookService.deleteBook(id).subscribe(() => {
      this.changePage();
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
