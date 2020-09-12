import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

@Component({
  selector: 'app-book-tag',
  templateUrl: './book-tag.component.html',
  styleUrls: ['./book-tag.component.scss'],
})
export class BookTagComponent implements OnInit {
  @Output() public deleteBookTag = new EventEmitter<string>();
  @Output() public searchTag = new EventEmitter<string>();
  @Input() public tag: string;

  constructor() {
  }

  ngOnInit(): void {
  }
}
