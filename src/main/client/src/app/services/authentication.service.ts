import { Injectable } from '@angular/core';
import { ConfigurationService } from './configuration.service';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { catchError, filter, finalize, first, map, tap } from 'rxjs/operators';
import { AuthenticationResponse } from '../models/authentication-response';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private authenticationResponse: AuthenticationResponse;
  private readonly loadingSubject: Subject<boolean>;
  private isAuthenticated: boolean;

  constructor(
    private configurationService: ConfigurationService,
    private httpClient: HttpClient,
    private router: Router
  ) {
    this.loadingSubject = new BehaviorSubject<boolean>(true);
    this.configurationService.loaded$
      .pipe(
        filter((l) => l),
        first()
      )
      .subscribe(() => {
        this.httpClient
          .get(
            `${this.configurationService.getConfiguration().apiBaseUrl}user`,
            { observe: 'response' }
          )
          .subscribe((response: HttpResponse<AuthenticationResponse>) => {
            if (response.status === 200) {
              this.isAuthenticated = true;
              this.authenticationResponse = response.body;
            }
            this.isAuthenticated = response.status === 200;
            this.loadingSubject.next(false);
          });
      });
  }

  public get loading$(): Observable<boolean> {
    return this.loadingSubject;
  }

  public authenticated(): boolean {
    return this.isAuthenticated;
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
          this.authenticationResponse = httpResponse.body;
          this.isAuthenticated = true;
        }),
        map(() => true),
        catchError(() => of(false)),
        finalize(() => this.loadingSubject.next(false))
      );
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
      this.authenticationResponse = null;
      this.isAuthenticated = false;
      this.router.navigate(['/login']);
    });
  }
}
