import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  public isMenuCollapsed = true;

  constructor(private authenticationService: AuthenticationService) {}

  ngOnInit(): void {}

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
