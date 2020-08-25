import { Component } from '@angular/core';
import { AuthenticationService } from './services/authentication.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'client';
  public isMenuCollapsed = false;

  constructor(private authenticationService: AuthenticationService) {}

  get username(): string {
    return this.authenticationService.getUsername();
  }
}

