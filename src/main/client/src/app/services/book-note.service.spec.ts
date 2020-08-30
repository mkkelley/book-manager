import { TestBed } from '@angular/core/testing';

import { BookNoteService } from './book-note.service';
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('BookNoteService', () => {
  let service: BookNoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(BookNoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
