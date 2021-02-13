import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewBookFormComponent } from './new-book-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ReactiveFormsModule } from '@angular/forms';

describe('NewBookFormComponent', () => {
  let component: NewBookFormComponent;
  let fixture: ComponentFixture<NewBookFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgbModule, ReactiveFormsModule],
      declarations: [NewBookFormComponent],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewBookFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
