import { TestBed } from '@angular/core/testing';

import { ConfigurationGuard } from './configuration.guard';
import { RouterTestingModule } from '@angular/router/testing';

describe('ConfigurationGuard', () => {
  let guard: ConfigurationGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
    });
    guard = TestBed.inject(ConfigurationGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
