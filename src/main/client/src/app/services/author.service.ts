import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';
import { Author } from '../models/author';

@Injectable({
  providedIn: 'root',
})
export class AuthorService {
  constructor(
    private httpClient: HttpClient,
    private configurationService: ConfigurationService
  ) {}

  public getAuthorTypeahead(input: string): Observable<Author[]> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }authors/typeahead?author=${encodeURIComponent(input)}`;
    return this.httpClient.get<Author[]>(route);
  }
}
