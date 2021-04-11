import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BookDetailComponent } from './book-detail.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Component, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { BookStorageService } from '../../services/book-storage.service';
import { ConfigurationService } from '../../services/configuration.service';
import SpyObj = jasmine.SpyObj;

describe('BookDetailComponent', () => {
  let component: BookDetailComponent;
  let fixture: ComponentFixture<BookDetailComponent>;
  let mockConfigurationService: SpyObj<ConfigurationService>;

  beforeEach(async(() => {
    const mockBookStorageService = jasmine.createSpyObj(['getFiles']);
    mockConfigurationService = jasmine.createSpyObj<ConfigurationService>([
      'getConfiguration',
      'loaded$',
    ]);
    mockConfigurationService.getConfiguration.and.returnValue({
      storageEnabled: false,
      apiBaseUrl: '',
    });
    const routeStub = {
      data: of({
        book: {
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
          notes: [],
        },
      }),
    };
    TestBed.configureTestingModule({
      imports: [RouterTestingModule, HttpClientTestingModule],
      declarations: [
        BookDetailComponent,
        MockBookNoteFormComponent,
        MockInlineEditComponent,
        MockBookTagListComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: routeStub,
        },
        {
          provide: BookStorageService,
          useValue: mockBookStorageService,
        },
        {
          provide: ConfigurationService,
          useValue: mockConfigurationService,
        },
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BookDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not show uploader', () => {
    expect(component.storageEnabled).toBeFalse();
    const compiledTemplate = fixture.debugElement.nativeElement;
    const element: Element | null = compiledTemplate.querySelector(
      'input[type=file]'
    );
    expect(element).toBeNull();
  });

  it('should show uploader when enabled', () => {
    mockConfigurationService.getConfiguration.and.returnValue({
      apiBaseUrl: '',
      storageEnabled: true,
    });
    fixture = TestBed.createComponent(BookDetailComponent);
    fixture.detectChanges();
    component = fixture.componentInstance;
    expect(component.storageEnabled).toBeTrue();
    const compiledTemplate = fixture.debugElement.nativeElement;
    const element: Element | null = compiledTemplate.querySelector(
      'input[type=file]'
    );
    expect(element).toBeTruthy();
  });

  it('should always show the form to create a note', () => {
    const compiledTemplate = fixture.debugElement.nativeElement;
    const element: Element | null = compiledTemplate.querySelector(
      'app-book-note-form'
    );
    expect(element).toBeTruthy();
  });
});

@Component({
  selector: 'app-book-note-form',
  template: '',
})
class MockBookNoteFormComponent {}

@Component({
  selector: 'app-inline-edit',
  template: '',
})
class MockInlineEditComponent {
  @Input() value: any;
}

@Component({
  selector: 'app-book-tag-list',
  template: '',
})
class MockBookTagListComponent {
  @Input() tags: any;
}
