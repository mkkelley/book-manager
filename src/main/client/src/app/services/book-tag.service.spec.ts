import { TestBed } from '@angular/core/testing';

import { BookTagService } from './book-tag.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookTagService', () => {
  let service: BookTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(BookTagService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
