import { TestBed } from '@angular/core/testing';

import { BookNoteService } from './book-note.service';

describe('BookNoteService', () => {
  let service: BookNoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookNoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
