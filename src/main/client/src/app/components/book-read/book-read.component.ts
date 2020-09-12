import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { BookRead } from '../../models/book-read';

@Component({
  selector: 'app-book-read',
  templateUrl: './book-read.component.html',
  styleUrls: ['./book-read.component.scss'],
})
export class BookReadComponent implements OnInit {
  @Input() bookRead: BookRead;
  @Output() finish = new EventEmitter();
  @Output() delete = new EventEmitter();

  constructor() {}

  ngOnInit(): void {}

  emitFinish(): void {
    this.finish.emit();
  }

  emitDelete(): void {
    this.delete.emit();
  }
}
