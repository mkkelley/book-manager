import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { catchError, finalize, map, tap } from 'rxjs/operators';
import { AuthenticationResponse } from '../models/authentication-response';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private sessionId: string;
  private authenticationResponse: AuthenticationResponse;
  private timestamp: number;
  private readonly FIFTEEN_MINUTES_IN_MS = 15 * 60 * 1000;
  private readonly loadingSubject: Subject<boolean>;

  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient,
    private router: Router
  ) {
    this.loadingSubject = new BehaviorSubject<boolean>(true);
    const storageToken: string = window.localStorage.getItem('auth-token');
    const storageTimestamp: number = +window.localStorage.getItem(
      'auth-token-timestamp'
    );
    const currentTime = new Date().getTime();
    if (
      storageToken != null &&
      currentTime - storageTimestamp < this.FIFTEEN_MINUTES_IN_MS
    ) {
      const xhr = new XMLHttpRequest();
      xhr.addEventListener(
        'load',
        this.loadInitialState.bind(this, storageToken, currentTime)
      );
      xhr.open(
        'GET',
        this.configurationService.getConfiguration().apiBaseUrl + 'user'
      );
      xhr.setRequestHeader('X-Auth-Token', storageToken);
      xhr.send();
    } else {
      this.loadingSubject.next(false);
    }
  }

  public get loading$() {
    return this.loadingSubject;
  }

  private loadInitialState(token, time, xxx: ProgressEvent<XMLHttpRequest>) {
    this.sessionId = token;
    this.timestamp = time;
    window.localStorage.setItem('auth-token-timestamp', time);
    this.authenticationResponse = JSON.parse(xxx.target.response);
    this.loadingSubject.next(false);
  }

  public getSessionId(): string | null {
    const currentTime = new Date().getTime();
    if (currentTime - this.timestamp > this.FIFTEEN_MINUTES_IN_MS) {
      this.router.navigate(['/']);
      return null;
    }
    this.timestamp = currentTime;
    return this.sessionId;
  }

  public getUsername(): string | null {
    return this.authenticationResponse?.name;
  }

  public authenticate(username: string, password: string): Observable<boolean> {
    this.loadingSubject.next(true);
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
          this.sessionId = httpResponse.headers.get('X-Auth-Token');
          this.timestamp = new Date().getTime();
          window.localStorage.setItem('auth-token', this.sessionId);
          window.localStorage.setItem(
            'auth-token-timestamp',
            this.timestamp.toString()
          );
          this.authenticationResponse = httpResponse.body;
        }),
        map(() => true),
        catchError(() => of(false)),
        finalize(() => this.loadingSubject.next(false))
      );
  }
}
