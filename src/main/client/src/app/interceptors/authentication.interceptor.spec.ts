import { TestBed } from '@angular/core/testing';

import { AuthenticationInterceptor } from './authentication.interceptor';
import { AuthenticationService } from '../services/authentication.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('AuthenticationInterceptor', () => {
  beforeEach(() =>
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule],
      providers: [AuthenticationInterceptor, AuthenticationService],
    })
  );

  it('should be created', () => {
    const interceptor: AuthenticationInterceptor = TestBed.inject(
      AuthenticationInterceptor
    );
    expect(interceptor).toBeTruthy();
  });
});
