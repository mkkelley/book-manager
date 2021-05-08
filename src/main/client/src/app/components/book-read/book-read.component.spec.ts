import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import {BookReadComponent} from './book-read.component';

describe('BookReadComponent', () => {
  let component: BookReadComponent;
  let fixture: ComponentFixture<BookReadComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [BookReadComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookReadComponent);
    component = fixture.componentInstance;
    component.bookRead = {
      finished: new Date().getTime(),
      id: 'asdf',
      audiobook: true,
      bookId: 123,
      started: new Date().getTime(),
      username: 'asdflkjasdfl',
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
