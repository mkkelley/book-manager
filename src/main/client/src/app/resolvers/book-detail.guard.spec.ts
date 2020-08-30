import { TestBed } from '@angular/core/testing';

import { BookDetailResolver } from './book-detail-resolver.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookDetailGuard', () => {
  let guard: BookDetailResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({ imports: [HttpClientTestingModule] });
    guard = TestBed.inject(BookDetailResolver);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
