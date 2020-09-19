import { TestBed } from '@angular/core/testing';

import { BookStorageService } from './book-storage.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookStorageService', () => {
  let service: BookStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    service = TestBed.inject(BookStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
