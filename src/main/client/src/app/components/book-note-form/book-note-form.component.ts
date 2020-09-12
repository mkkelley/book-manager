import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BookNote } from '../../models/book-note';
import { FormControl, FormGroup } from '@angular/forms';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-book-note-form',
  templateUrl: './book-note-form.component.html',
  styleUrls: ['./book-note-form.component.scss'],
})
export class BookNoteFormComponent implements OnInit {
  @Output() note = new EventEmitter<Partial<BookNote>>();
  @Input() clear: Observable<any>;
  public form: FormGroup;

  constructor() {}

  ngOnInit(): void {
    this.form = new FormGroup({
      notes: new FormControl(''),
    });
    if (this.clear) {
      this.clear.subscribe(() => {
        this.form = new FormGroup({
          notes: new FormControl(''),
        });
      });
    }
  }

  submit(): void {
    this.note.emit({
      notes: this.form.controls.notes.value,
    });
  }
}
