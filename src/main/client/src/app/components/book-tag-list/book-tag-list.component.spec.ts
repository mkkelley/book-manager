import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookTagListComponent } from './book-tag-list.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('BookTagListComponent', () => {
  let component: BookTagListComponent;
  let fixture: ComponentFixture<BookTagListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BookTagListComponent],
      imports: [HttpClientTestingModule],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookTagListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
