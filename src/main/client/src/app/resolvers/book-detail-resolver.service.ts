import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Observable } from 'rxjs';
import { BookDetail } from '../models/book-detail';
import { HttpClient } from '@angular/common/http';
import { ConfigurationService } from '../services/configuration.service';
import { first } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class BookDetailResolver implements Resolve<BookDetail> {
  constructor(
    private httpClient: HttpClient,
    private configurationService: ConfigurationService
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<BookDetail> | Promise<BookDetail> | BookDetail {
    const id = route.paramMap.get('bookId');
    const rt = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${id}`;
    return this.httpClient.get<BookDetail>(rt).pipe(first());
  }
}
