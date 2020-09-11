import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';
import { Observable } from 'rxjs';
import { Book } from '../models/book';

@Injectable({
  providedIn: 'root',
})
export class BookTagService {
  constructor(
    private httpClient: HttpClient,
    private configurationService: ConfigurationService
  ) {}

  addBookTag(bookId: number, tag: string): Observable<Book> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${bookId}/tags`;
    return this.httpClient.put<Book>(route, [tag]);
  }

  removeBookTag(bookId: number, tag: string): Observable<Book> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${bookId}/tags/${tag}`;
    return this.httpClient.delete<Book>(route);
  }
}
