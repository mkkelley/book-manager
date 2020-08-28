import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AddBookRequest } from '../../models/add-book-request';
import { Observable } from 'rxjs';
import { AuthorService } from '../../services/author.service';
import {
  debounceTime,
  distinctUntilChanged,
  map,
  switchMap,
} from 'rxjs/operators';

@Component({
  selector: '[app-new-book-form]',
  templateUrl: './new-book-form.component.html',
  styleUrls: ['./new-book-form.component.scss'],
})
export class NewBookFormComponent implements OnInit {
  @Output() newBook = new EventEmitter<AddBookRequest>();
  bookForm: FormGroup;

  constructor(private authorService: AuthorService) {}

  ngOnInit(): void {
    this.bookForm = new FormGroup({
      title: new FormControl(''),
      authorName: new FormControl(''),
      published: new FormControl(''),
    });
  }

  submit() {
    const date: { day: number; month: number; year: number } = this.bookForm
      .controls.published.value;
    this.newBook.emit({
      title: this.bookForm.controls.title.value,
      authorName: this.bookForm.controls.authorName.value,
      published: new Date(date.year, date.month - 1, date.day).getTime(),
    });
  }

  typeahead(authorName$: Observable<string>): Observable<string[]> {
    return authorName$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      switchMap((authorName) => {
        return this.authorService.getAuthorTypeahead(authorName).pipe(
          debounceTime(200),
          distinctUntilChanged(),
          map((a) => a.map((au) => au.name))
        );
      })
    );
  }
}
