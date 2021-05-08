import { TestBed, waitForAsync } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { Component } from '@angular/core';

describe('AppComponent', () => {
  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      declarations: [
        AppComponent,
        MockAppBookIndexComponent,
        MockAppLoginComponent,
        MockNavbarComponent,
      ],
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});

@Component({
  selector: 'app-book-index',
  template: '',
})
class MockAppBookIndexComponent {}

@Component({
  selector: 'app-login',
  template: '',
})
class MockAppLoginComponent {}

@Component({
  selector: 'app-navbar',
  template: '',
})
class MockNavbarComponent {}
