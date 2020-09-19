import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-inline-edit',
  templateUrl: './inline-edit.component.html',
  styleUrls: ['./inline-edit.component.scss'],
})
export class InlineEditComponent implements OnInit, OnChanges {
  @Input() public inputClass = 'form-control';
  @Input() public value = '';
  @Input() public placeholder = '';
  @Output() public valueChange = new EventEmitter<string>();
  @ViewChild('textbox') textbox: ElementRef;
  public editMode = false;
  public formValue: string;

  constructor() {}

  ngOnInit(): void {
    this.formValue = this.value;
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (
      !this.editMode &&
      changes != null &&
      changes.value.currentValue !== changes.value.previousValue
    ) {
      this.formValue = changes.value.currentValue;
    }
  }

  submit(): void {
    this.valueChange.emit(this.formValue);
    this.editMode = false;
  }

  cancel(): void {
    this.editMode = false;
    this.formValue = this.value;
  }

  edit(): void {
    this.editMode = true;
    setTimeout(() => this.textbox.nativeElement.focus());
  }
}
