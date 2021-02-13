import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BookIndexComponent } from './components/book-index/book-index.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { LoginComponent } from './components/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { AuthenticationInterceptor } from './interceptors/authentication.interceptor';
import { NewBookFormComponent } from './components/new-book-form/new-book-form.component';
import { BookReadComponent } from './components/book-read/book-read.component';
import { BookComponent } from './components/book/book.component';
import { BookDetailComponent } from './components/book-detail/book-detail.component';
import { BookNoteFormComponent } from './components/book-note-form/book-note-form.component';
import { BookTagComponent } from './components/book-tag/book-tag.component';
import { BookTagListComponent } from './components/book-tag-list/book-tag-list.component';
import { InlineEditComponent } from './components/inline-edit/inline-edit.component';
import { NavbarComponent } from './components/navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    BookIndexComponent,
    LoginComponent,
    NewBookFormComponent,
    BookReadComponent,
    BookComponent,
    BookDetailComponent,
    BookNoteFormComponent,
    BookTagComponent,
    BookTagListComponent,
    InlineEditComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthenticationInterceptor,
      multi: true,
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
