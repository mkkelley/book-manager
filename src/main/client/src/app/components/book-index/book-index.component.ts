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
  public searchControl = new FormControl('');

  private destroy$ = new Subject();

  constructor(
    private bookService: BookService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    route.queryParamMap.subscribe((paramMap) => {
                                                  let changed = false;
                                                  if (
                                                    paramMap.has('page') &&
                                                    this.page !==
                                                      +paramMap.get('page')
                                                  ) {
                                                    this.page = +paramMap.get(
                                                      'page'
                                                    );
                                                    changed = true;
                                                  }
                                                  if (
                                                    paramMap.has('size') &&
                                                    this.size !==
                                                      +paramMap.get('size')
                                                  ) {
                                                    this.size = +paramMap.get(
                                                      'size'
                                                    );
                                                    changed = true;
                                                  }
                                                  if (
                                                    (paramMap.has('tag') &&
                                                      this.tag !==
                                                        paramMap.get('tag')) ||
                                                    (!paramMap.has('tag') &&
                                                      this.tag != null &&
                                                      this.tag !== '')
                                                  ) {
                                                    this.tag = paramMap.get(
                                                      'tag'
                                                    );
                                                    changed = true;
                                                  }
                                                  if (
                                                    (paramMap.has('search') &&
                                                      this.searchControl
                                                        .value !==
                                                        paramMap.get(
                                                          'search'
                                                        )) ||
                                                    (!paramMap.has('search') &&
                                                      this.searchControl
                                                        .value != null &&
                                                      this.searchControl
                                                        .value !== '')
                                                  ) {
                                                    this.searchControl.setValue(
                                                      paramMap.get('search')
                                                    );
                                                    changed = true;
                                                  }

                                                  if (changed) {
                                                    this.changePage();
                                                  }
                                                });
  }

  ngOnInit(): void {
    this.changePage();
    this.newBooks = [];
    this.searchControl.valueChanges
      .pipe(debounceTime(300), takeUntil(this.destroy$))
      .subscribe(() => this.changePage());
  }

  changePage(): void {
    this.newBooks = [];
    let queryParams: {} = {
      page: this.page,
      size: this.size,
    };
    const search = this.searchControl.value;
    const tag = this.tag;
    this.books$ = this.bookService.searchBooks(
      search,
      tag,
      this.page,
      this.size
    );
    queryParams = {
      ...queryParams,
      search: search != null && search !== '' ? search : null,
      tag: tag != null && tag !== '' ? tag : null,
    };
    this.router.navigate([], {
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

  nextPage(): void {
    this.page = this.page + 1;
    this.changePage();
  }

  prevPage(): void {
    this.page = this.page - 1;
    this.changePage();
  }

  removeTagSearch(): void {
    this.tag = null;
    this.changePage();
  }
}
