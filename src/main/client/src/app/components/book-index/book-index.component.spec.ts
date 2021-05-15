import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BookIndexComponent } from './book-index.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { Component } from '@angular/core';
import { BookService } from '../../services/book.service';
import { ReactiveFormsModule } from '@angular/forms';
import { AddBookRequest } from '../../models/add-book-request';
import { of } from 'rxjs';
import { Book } from '../../models/book';

describe('BookIndexComponent', () => {
  let component: BookIndexComponent;
  let fixture: ComponentFixture<BookIndexComponent>;
  let mockBookService;

  beforeEach(
    waitForAsync(() => {
      mockBookService = jasmine.createSpyObj<BookService>([
        'searchBooks',
        'createBook',
      ]);
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
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(BookIndexComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should allow users to create book forms', () => {
    expect(component.newBooks).toBeTruthy();
    const numNewBooks = component.newBooks.length;
    component.newBook();
    expect(component.newBooks.length).toEqual(numNewBooks + 1);
    expect(
      component.newBooks[component.newBooks.length - 1].created
    ).toBeFalse();
  });

  it('should allow users to remove book forms', () => {
    expect(component.newBooks).toBeTruthy();
    component.newBook();

    const newBook = component.newBooks[0];
    expect(newBook).toBeTruthy();

    component.removeBookForm(newBook);
    expect(component.newBooks.length).toEqual(0);
  });

  it('should allow users to create books', () => {
    component.newBook();
    const newBook = component.newBooks[component.newBooks.length - 1];
    const request: AddBookRequest = {
      authorName: 'author',
      title: 'title',
      published: null,
    };
    const book: Book = {
      bookReads: [],
      title: 'title',
      id: 123,
      author: 'author',
      published: null,
      createdAt: new Date().getTime(),
      tags: [],
    };
    mockBookService.createBook.and.returnValue(of(book));

    const newBooks = component.newBooks;
    component.createBook(newBook, request);

    expect(mockBookService.createBook).toHaveBeenCalledTimes(1);
    expect(component.newBooks).not.toBe(newBooks);
    expect(newBook.book).toBe(book);
    expect(newBook.created).toBeTruthy();
  });
});

@Component({
  selector: 'app-book-form',
  template: '',
})
class MockAppBookFormComponent {}
