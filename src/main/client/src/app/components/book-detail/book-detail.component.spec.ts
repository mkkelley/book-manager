import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookDetailComponent } from './book-detail.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { BookStorageService } from '../../services/book-storage.service';

describe('BookDetailComponent', () => {
  let component: BookDetailComponent;
  let fixture: ComponentFixture<BookDetailComponent>;

  beforeEach(async(() => {
    const mockBookStorageService = jasmine.createSpyObj(['getFiles']);
    const routeStub = {
      data: of({
        book: {
          bookReads: [],
          title: 'asdfasdf',
          author: {
            name: 'asdfasdfasdf',
            id: 1234,
            createdAt: new Date().getTime(),
          },
          id: 1231231,
          published: new Date().getTime(),
          createdAt: new Date().getTime(),
          notes: [],
        },
      }),
    };
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [BookDetailComponent, MockBookNoteFormComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: routeStub,
        },
        {
          provide: BookStorageService,
          useValue: mockBookStorageService,
        },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

@Component({
  selector: 'app-book-note-form',
  template: '',
})
class MockBookNoteFormComponent {}
