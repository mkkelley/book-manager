import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AuthenticationService } from '../../services/authentication.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class LoginComponent implements OnInit {
  public loginForm: FormGroup;
  public loginFailed: boolean;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      username: new FormControl(''),
      password: new FormControl(''),
    });

    this.loginFailed = false;
  }

  submit() {
    const username = this.loginForm.value.username;
    const password = this.loginForm.value.password;
    this.authenticationService
      .authenticate(username, password)
      .subscribe((result) => {
        if (result === true) {
          this.loginFailed = false;
          this.router.navigate(['/']);
        } else {
          this.loginFailed = true;
        }
      });
  }
}
