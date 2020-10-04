import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';
import { filter, first, map, mergeMap } from 'rxjs/operators';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    return this.authenticationService.loading$.pipe(
      filter((loading) => {
        if (request.headers.get('Authorization') != null) {
          return true;
        }
        return !loading;
      }),
      first(),
      map(() => {
        const token = this.authenticationService.getSessionId();
        if (token != null) {
          return request.clone({
            setHeaders: {
              'X-Auth-Token': token,
            },
          });
        } else {
          return request;
        }
      }),
      mergeMap((newRequest) => next.handle(newRequest))
    );
  }
}
