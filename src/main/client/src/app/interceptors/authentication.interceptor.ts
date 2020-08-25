import { Injectable } from '@angular/core';
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthenticationService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const token = this.authenticationService.getSessionId();
    // request.headers.set('X-Auth-Token', token);
    let newRequest;
    if (token != null) {
      newRequest = request.clone({
        setHeaders: {
          'X-Auth-Token': token,
        },
      });
    } else {
      newRequest = request;
    }
    return next.handle(newRequest);
  }
}
