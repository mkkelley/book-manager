import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { ConfigurationService } from './configuration.service';

@Injectable({
  providedIn: 'root',
})
export class TagService {
  constructor(
    private httpClient: HttpClient,
    private configurationService: ConfigurationService
  ) {}

  public typeahead(search: string): Observable<string[]> {
    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }tags/typeahead?tag=${search}`;
    return this.httpClient.get<string[]>(route);
  }
}
