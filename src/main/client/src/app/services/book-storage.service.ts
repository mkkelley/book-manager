import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookStorage } from '../models/book-storage';

@Injectable({
  providedIn: 'root',
})
export class BookStorageService {
  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient
  ) {}

  public getFiles(bookId: number): Observable<BookStorage[]> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }storage/books/${bookId}`;

    return this.httpClient.get<BookStorage[]>(route);
  }

  public uploadFile(file: File, bookId: number): Observable<BookStorage> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }storage/books/${bookId}`;

    const formData = new FormData();
    formData.append('file', file, file.name);

    return this.httpClient.post<BookStorage>(route, formData);
  }
}
