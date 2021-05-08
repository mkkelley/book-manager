import { Component, EventEmitter, Input, Output } from '@angular/core';
import { BookRead } from '../../models/book-read';

@Component({
  selector: 'app-book-read',
  templateUrl: './book-read.component.html',
  styleUrls: ['./book-read.component.scss'],
})
export class BookReadComponent {
  @Input() bookRead: BookRead;
  @Output() finish = new EventEmitter();
  @Output() delete = new EventEmitter();

  constructor() {}

  emitFinish(): void {
    this.finish.emit();
  }

  emitDelete(): void {
    this.delete.emit();
  }
}
