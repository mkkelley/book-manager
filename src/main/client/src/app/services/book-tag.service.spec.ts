import { TestBed } from '@angular/core/testing';

import { BookTagService } from './book-tag.service';

describe('BookTagService', () => {
  let service: BookTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookTagService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
