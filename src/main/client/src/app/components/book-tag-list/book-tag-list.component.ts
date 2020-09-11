import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { TagService } from '../../services/tag.service';

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

  constructor(private tagService: TagService) {
    this.addTagMode = false;
  }

  ngOnInit(): void {}

  submit(tag: string) {
    this.addTagMode = false;
    this.createBookTag.emit(tag);
  }

  typeahead(tagName$: Observable<string>): Observable<string[]> {
    return tagName$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      switchMap((tag) => {
        return this.tagService
          .typeahead(tag)
          .pipe(debounceTime(200), distinctUntilChanged());
      })
    );
  }
}
