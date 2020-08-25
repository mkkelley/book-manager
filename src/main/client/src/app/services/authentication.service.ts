import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { AuthenticationResponse } from '../models/authentication-response';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private sessionId: string;
  private authenticationResponse: AuthenticationResponse;

  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient
  ) {}

  public getSessionId(): string | null {
    return this.sessionId;
  }

  public getUsername(): string | null {
    return this.authenticationResponse?.name;
  }

  public authenticate(username: string, password: string): Observable<boolean> {
    return this.httpClient
      .get<AuthenticationResponse>(
        this.configurationService.getConfiguration().apiBaseUrl + 'user',
        {
          headers: new HttpHeaders({
            Authorization: 'Basic ' + btoa(`${username}:${password}`),
          }),
          observe: 'response',
        }
      )
      .pipe(
        tap((httpResponse) => {
          console.log(httpResponse);
          this.sessionId = httpResponse.headers.get('X-Auth-Token');
          console.log(this.sessionId);
          this.authenticationResponse = httpResponse.body;
        }),
        map(() => true),
        catchError(() => of(false))
      );
  }
}
