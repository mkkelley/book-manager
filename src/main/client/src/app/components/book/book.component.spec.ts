import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookComponent } from './book.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookComponent', () => {
  let component: BookComponent;
  let fixture: ComponentFixture<BookComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [BookComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookComponent);
    component = fixture.componentInstance;
    component.book = {
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
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
