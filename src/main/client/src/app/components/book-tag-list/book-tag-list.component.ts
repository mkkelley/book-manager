import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnDestroy,
  Output,
  ViewChild,
} from '@angular/core';
import { Observable, Subject } from 'rxjs';
import {
  debounceTime,
  distinctUntilChanged,
  switchMap,
  takeUntil,
} from 'rxjs/operators';
import { TagService } from '../../services/tag.service';
import { Router } from '@angular/router';
import { DEFAULT_PAGE_SIZE } from '../../app.constants';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-book-tag-list',
  templateUrl: './book-tag-list.component.html',
  styleUrls: ['./book-tag-list.component.scss'],
})
export class BookTagListComponent implements OnDestroy {
  @Input() public tags: string[];
  @Output() public createBookTag = new EventEmitter<string>();
  @Output() public deleteBookTag = new EventEmitter<string>();
  @ViewChild('tagName') public tagName: ElementRef;
  private destroy$ = new Subject<void>();

  public tagInputControl: FormControl;
  public tagTypeahead$: Observable<string[]>;
  public addTagMode: boolean;

  constructor(private tagService: TagService, private router: Router) {
    this.addTagMode = false;
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }

  submit(tag: string): void {
    this.addTagMode = false;
    this.tagInputControl = null;
    this.createBookTag.emit(tag);
  }

  enterCreateMode(): void {
    this.tagInputControl = new FormControl('');
    this.tagTypeahead$ = this.typeahead(this.tagInputControl.valueChanges);
    this.addTagMode = true;
    setTimeout(() => this.tagName.nativeElement.focus());
  }

  typeahead(tagName$: Observable<string>): Observable<string[]> {
    return tagName$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      takeUntil(this.destroy$),
      switchMap((tag) => {
        return this.tagService
          .typeahead(tag)
          .pipe(debounceTime(200), distinctUntilChanged());
      })
    );
  }

  searchTag(tag: string): void {
    this.router.navigate(['/books'], {
      queryParams: {
        page: 0,
        size: DEFAULT_PAGE_SIZE,
        tag: tag,
      },
    });
  }
}
