import { TestBed } from '@angular/core/testing';

import { BookDetailResolver } from './book-detail-resolver.service';

describe('BookDetailGuard', () => {
  let guard: BookDetailResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(BookDetailResolver);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
