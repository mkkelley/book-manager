import { TestBed } from '@angular/core/testing';

import { BookPageResolver } from './book-page-resolver.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookPageResolverService', () => {
  let service: BookPageResolver;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(BookPageResolver);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
