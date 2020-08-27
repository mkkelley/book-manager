import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { AddBookRequest } from '../../models/add-book-request';

@Component({
  selector: '[app-new-book-form]',
  templateUrl: './new-book-form.component.html',
  styleUrls: ['./new-book-form.component.scss'],
})
export class NewBookFormComponent implements OnInit {
  @Output() newBook = new EventEmitter<AddBookRequest>();
  bookForm: FormGroup;

  constructor() {}

  ngOnInit(): void {
    this.bookForm = new FormGroup({
      title: new FormControl(''),
      authorName: new FormControl(''),
      published: new FormControl(''),
    });
  }

  submit() {
    this.newBook.emit({
      title: this.bookForm.controls.title.value,
      authorName: this.bookForm.controls.authorName.value,
      published: new Date(this.bookForm.controls.published.value).getTime(),
    });
  }
}
