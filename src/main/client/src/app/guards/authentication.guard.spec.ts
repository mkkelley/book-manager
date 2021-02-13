import { TestBed } from '@angular/core/testing';

import { AuthenticationGuard } from './authentication.guard';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';

describe('AuthenticationGuard', () => {
  let guard: AuthenticationGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule.withRoutes([
          { path: '', redirectTo: 'login', pathMatch: 'full' },
          { path: 'login', component: MockLoginComponent },
        ]),
      ],
      providers: [],
    });
    guard = TestBed.inject(AuthenticationGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});

@Component({
  selector: 'app-login',
  template: '',
})
class MockLoginComponent {}
