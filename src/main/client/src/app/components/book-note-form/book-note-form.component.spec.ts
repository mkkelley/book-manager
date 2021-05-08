import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BookNoteFormComponent } from './book-note-form.component';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';

describe('BookNoteFormComponent', () => {
  let component: BookNoteFormComponent;
  let fixture: ComponentFixture<BookNoteFormComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, RouterTestingModule],
      declarations: [BookNoteFormComponent],
    }).compileComponents();
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
