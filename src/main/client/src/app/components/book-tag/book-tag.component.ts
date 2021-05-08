import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-book-tag',
  templateUrl: './book-tag.component.html',
  styleUrls: ['./book-tag.component.scss'],
})
export class BookTagComponent {
  @Output() public deleteBookTag = new EventEmitter<string>();
  @Output() public searchTag = new EventEmitter<string>();
  @Input() public tag: string;

  constructor() {}
}
