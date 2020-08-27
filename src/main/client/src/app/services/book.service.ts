import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagedResult } from '../models/paged-result';
import { Book } from '../models/book';

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
}
