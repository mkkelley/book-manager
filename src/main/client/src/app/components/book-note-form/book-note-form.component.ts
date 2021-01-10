import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { BookNote } from '../../models/book-note';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-book-note-form',
  templateUrl: './book-note-form.component.html',
  styleUrls: ['./book-note-form.component.scss'],
})
export class BookNoteFormComponent implements OnInit {
  @Output() note = new EventEmitter<Partial<BookNote>>();
  public form: FormGroup;

  constructor() {}

  ngOnInit(): void {
    this.form = new FormGroup({
      notes: new FormControl('', [Validators.required]),
    });
  }

  submit(): void {
    if (!this.form.valid) {
      Object.keys(this.form.controls).forEach((field) =>
        this.form.get(field).markAsTouched({ onlySelf: true })
      );
      return;
    }

    this.note.emit({
      notes: this.form.controls.notes.value,
    });
    this.form.controls.notes.setValue('');
    Object.keys(this.form.controls).forEach((field) => {
      this.form.get(field).markAsUntouched();
      this.form.get(field).markAsPristine();
    });
  }
}
