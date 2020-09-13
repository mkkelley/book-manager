import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedResult } from '../models/paged-result';
import { Book } from '../models/book';
import { AddBookRequest } from '../models/add-book-request';
import { AddBookReadRequest } from '../models/add-book-read-request';
import { BookRead } from '../models/book-read';
import { FinishBookReadRequest } from '../models/finish-book-read-request';

@Injectable({
  providedIn: 'root',
})
export class BookService {
  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient
  ) {}

  public getBooks(page = 0, size = 20): Observable<PagedResult<Book>> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books?page=${page}&size=${size}`;
    return this.httpClient.get<PagedResult<Book>>(route);
  }

  public searchBooks(
    search: string,
    tag: string,
    unfinished: boolean,
    page = 0,
    size = 20
  ): Observable<PagedResult<Book>> {
    let route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books?page=${page}&size=${size}`;
    if (search != null && search !== '') {
      route = route + `&search=${search}`;
    }
    if (tag != null && tag !== '') {
      route = route + `&tag=${tag}`;
    }
    if (unfinished != null) {
      route = route + `&unfinished=${unfinished}`;
    }
    return this.httpClient.get<PagedResult<Book>>(route);
  }

  public createBook(request: AddBookRequest): Observable<Book> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books`;
    return this.httpClient.post<Book>(route, request);
  }

  public deleteBook(id: number): Observable<Book> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${id}`;
    return this.httpClient.delete<Book>(route);
  }

  public createBookRead(request: AddBookReadRequest): Observable<BookRead> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${request.bookId}/reads`;
    return this.httpClient.post<BookRead>(route, request);
  }

  public finishBookRead(
    bookId: number,
    request: FinishBookReadRequest
  ): Observable<BookRead> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${bookId}/reads/${request.id}/finish`;
    return this.httpClient.post<BookRead>(route, request);
  }

  public deleteBookRead(bookId: number, id: string): Observable<BookRead> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${bookId}/reads/${id}`;
    return this.httpClient.delete<BookRead>(route);
  }
}
