import { Component } from '@angular/core';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  public isMenuCollapsed = true;

  constructor(private authenticationService: AuthenticationService) {}

  public username(): string {
    return this.authenticationService.getUsername();
  }

  public get authenticated(): boolean {
    return this.authenticationService.authenticated();
  }

  public logout(): void {
    this.authenticationService.logout();
  }
}
