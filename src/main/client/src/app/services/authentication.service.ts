import {Injectable} from '@angular/core';
import {ConfigurationService} from './configuration.service';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {BehaviorSubject, Observable, of, Subject} from 'rxjs';
import {catchError, filter, finalize, map, tap} from 'rxjs/operators';
import {AuthenticationResponse} from '../models/authentication-response';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private sessionId: string;
  private authenticationResponse: AuthenticationResponse;
  private timestamp: number;
  private readonly FIFTY_NINE_MINUTES_IN_MS = 59 * 60 * 1000;
  private readonly loadingSubject: Subject<boolean>;
  private readonly authTokenKey = 'auth-token';
  private readonly timestampKey = 'auth-token-timestamp';

  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient,
    private router: Router
  ) {
    this.loadingSubject = new BehaviorSubject<boolean>(true);
    const storageToken: string = window.localStorage.getItem(this.authTokenKey);
    const storageTimestamp: number = +window.localStorage.getItem(
      this.timestampKey
    );
    const currentTime = new Date().getTime();
    if (
      storageToken != null &&
      currentTime - storageTimestamp < this.FIFTY_NINE_MINUTES_IN_MS
    ) {
      this.configurationService.loaded$
        .pipe(filter((loaded) => loaded))
        .subscribe(() => {
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
        });
    } else {
      this.loadingSubject.next(false);
    }
  }

  public get loading$(): Observable<boolean> {
    return this.loadingSubject;
  }

  public authenticated(): boolean {
    return (
      this.sessionId != null &&
      new Date().getTime() - this.timestamp < this.FIFTY_NINE_MINUTES_IN_MS
    );
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
          window.localStorage.setItem(this.authTokenKey, this.sessionId);
          window.localStorage.setItem(
            this.timestampKey,
            this.timestamp.toString()
          );
          this.authenticationResponse = httpResponse.body;
        }),
        map(() => true),
        catchError(() => of(false)),
        finalize(() => this.loadingSubject.next(false))
      );
  }

  public getSessionId(): string | null {
    const currentTime = new Date().getTime();
    if (currentTime - this.timestamp > this.FIFTY_NINE_MINUTES_IN_MS) {
      this.router.navigate(['/login']);
      return null;
    }
    this.timestamp = currentTime;
    return this.sessionId;
  }

  public getUsername(): string | null {
    return this.authenticationResponse?.name;
  }

  public logout(): void {
    if (!this.authenticated()) {
      return;
    }

    const route = `${
      this.configurationService.getConfiguration().apiBaseUrl
    }logout`;

    this.httpClient.post(route, null).subscribe(() => {
      window.localStorage.removeItem(this.authTokenKey);
      window.localStorage.removeItem(this.timestampKey);
      this.sessionId = null;
      this.timestamp = null;
      this.authenticationResponse = null;
      this.router.navigate(['/login']);
    });
  }

  private loadInitialState(
    token,
    time,
    xxx: ProgressEvent<XMLHttpRequest>
  ): void {
    if (xxx.target.status === 200) {
      this.sessionId = token;
      this.timestamp = time;
      window.localStorage.setItem(this.timestampKey, time);
      this.authenticationResponse = JSON.parse(xxx.target.response);
      this.loadingSubject.next(false);
    }
  }
}
