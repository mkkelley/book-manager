import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BookNote } from '../models/book-note';

@Injectable({
  providedIn: 'root',
})
export class BookNoteService {
  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient
  ) {}

  public createNote(bookId: number, notes: string): Observable<BookNote> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }books/${bookId}/notes`;
    return this.httpClient.post<BookNote>(route, { notes, bookId });
  }
}
