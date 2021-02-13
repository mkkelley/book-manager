import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookIndexComponent } from './book-index.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';
import { BookService } from '../../services/book.service';
import { ReactiveFormsModule } from '@angular/forms';

describe('BookIndexComponent', () => {
  let component: BookIndexComponent;
  let fixture: ComponentFixture<BookIndexComponent>;

  beforeEach(async(() => {
    const mockBookService = jasmine.createSpyObj(['searchBooks']);
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule,
        ReactiveFormsModule,
      ],
      declarations: [BookIndexComponent, MockAppBookFormComponent],
      providers: [
        {
          provide: BookService,
          useValue: mockBookService,
        },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookIndexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

@Component({
  selector: 'app-book-form',
  template: '',
})
class MockAppBookFormComponent {}
