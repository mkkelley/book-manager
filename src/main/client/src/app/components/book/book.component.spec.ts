import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BookComponent } from './book.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, Input } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { BookTagService } from '../../services/book-tag.service';
import { of } from 'rxjs';
import SpyObj = jasmine.SpyObj;
import { BookService } from '../../services/book.service';
import { BookRead } from '../../models/book-read';

describe('BookComponent', () => {
  let component: BookComponent;
  let fixture: ComponentFixture<BookComponent>;
  let mockBookTagService: SpyObj<BookTagService>;
  let mockBookService: SpyObj<BookService>;

  beforeEach(
    waitForAsync(() => {
      mockBookTagService = jasmine.createSpyObj([
        'addBookTag',
        'removeBookTag',
      ]);
      mockBookService = jasmine.createSpyObj([
        'createBookRead',
        'finishBookRead',
        'deleteBookRead',
      ]);
      TestBed.configureTestingModule({
        imports: [
          HttpClientTestingModule,
          RouterTestingModule,
          ReactiveFormsModule,
        ],
        providers: [
          {
            provide: BookTagService,
            useValue: mockBookTagService,
          },
          {
            provide: BookService,
            useValue: mockBookService,
          },
        ],
        declarations: [BookComponent, MockAppBookTagListComponent],
      }).compileComponents();
    })
  );

  beforeEach(() => {
    fixture = TestBed.createComponent(BookComponent);
    component = fixture.componentInstance;
    component.book = {
      bookReads: [],
      title: 'asdfasdf',
      author: 'asdfasdfasdf',
      id: 1231231,
      published: new Date().getTime(),
      createdAt: new Date().getTime(),
      tags: ['a', 'b', 'c'],
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call book tag deletion', () => {
    mockBookTagService.removeBookTag.and.returnValue(of({ ...component.book }));
    const bk = component.book;
    component.deleteBookTag('tag', component.book);
    expect(mockBookTagService.removeBookTag).toHaveBeenCalledTimes(1);
    expect(component.book).not.toBe(bk);
  });

  it('should call book tag creation', () => {
    mockBookTagService.addBookTag.and.returnValue(of({ ...component.book }));
    const bk = component.book;
    component.createBookTag('tag', component.book);
    expect(mockBookTagService.addBookTag).toHaveBeenCalledTimes(1);
    expect(component.book).not.toBe(bk);
  });

  it('should call book read creation', () => {
    mockBookService.createBookRead.and.returnValue(of({} as BookRead));
    const bkReads = component.book.bookReads;
    component.newBookRead();
    expect(mockBookService.createBookRead).toHaveBeenCalledTimes(1);
    expect(component.book.bookReads).not.toBe(bkReads);
  });

  it('should call book read deletion', () => {
    mockBookService.deleteBookRead.and.returnValue(of({} as BookRead));
    const bkReads = component.book.bookReads;
    component.deleteBookRead('asdf', 123);
    expect(mockBookService.deleteBookRead).toHaveBeenCalledTimes(1);
    expect(component.book.bookReads).not.toBe(bkReads);
  });

  it('should call book read finishing', () => {
    component.book.bookReads = [
      {
        id: 'asdf',
        bookId: 123,
        finished: null,
      } as BookRead,
    ];
    mockBookService.finishBookRead.and.returnValue(
      of({ id: 'asdf' } as BookRead)
    );
    const bkReads = component.book.bookReads;
    component.finishBookRead('asdf', 123);
    expect(mockBookService.finishBookRead).toHaveBeenCalledTimes(1);
    expect(component.book.bookReads).not.toBe(bkReads);
  });

  it('can call delete', () => {
    spyOn(component.deleteBook, 'emit');
    component.delete();
    expect(component.deleteBook.emit).toHaveBeenCalledTimes(1);
  });
});

@Component({
  selector: 'app-book-tag-list',
  template: '',
})
class MockAppBookTagListComponent {
  @Input() tags: any;
}
