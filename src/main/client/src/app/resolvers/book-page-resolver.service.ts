import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Resolve,
  RouterStateSnapshot,
} from '@angular/router';
import { Book } from '../models/book';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ConfigurationService } from '../services/configuration.service';
import { PagedResult } from '../models/paged-result';
import { first } from 'rxjs/operators';
import { DEFAULT_PAGE_SIZE } from '../app.constants';

@Injectable({
  providedIn: 'root',
})
export class BookPageResolver implements Resolve<PagedResult<Book>> {
  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient
  ) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<PagedResult<Book>> | PagedResult<Book> {
    let params = new HttpParams();
    route.queryParamMap.keys.forEach(
      (k) => (params = params.append(k, route.queryParamMap.get(k)))
    );
    if (route.queryParamMap.get('page') == null) {
      params = params.append('page', '0');
    }
    if (route.queryParamMap.get('size') == null) {
      params = params.append('size', `${DEFAULT_PAGE_SIZE}`);
    }
    return this.httpClient
      .get<PagedResult<Book>>(
        `${this.configurationService.getConfiguration().apiBaseUrl}books`,
        {
          params: params,
        }
      )
      .pipe(first());
  }
}
