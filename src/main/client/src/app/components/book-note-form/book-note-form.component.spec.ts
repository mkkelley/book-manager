import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BookNoteFormComponent } from './book-note-form.component';

describe('BookNoteFormComponent', () => {
  let component: BookNoteFormComponent;
  let fixture: ComponentFixture<BookNoteFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BookNoteFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookNoteFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
