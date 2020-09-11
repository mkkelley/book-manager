import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-book-tag-list',
  templateUrl: './book-tag-list.component.html',
  styleUrls: ['./book-tag-list.component.scss'],
})
export class BookTagListComponent implements OnInit {
  @Input() public tags: string[];
  @Output() public createBookTag = new EventEmitter<string>();
  @Output() public deleteBookTag = new EventEmitter<string>();

  public addTagMode: boolean;

  constructor() {
    this.addTagMode = false;
  }

  ngOnInit(): void {}

  submit(tag: string) {
    this.addTagMode = false;
    this.createBookTag.emit(tag);
  }
}
