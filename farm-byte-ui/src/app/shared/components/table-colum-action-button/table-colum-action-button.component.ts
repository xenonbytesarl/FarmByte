import {ChangeDetectionStrategy, Component, model} from '@angular/core';
import {Button} from "primeng/button";

@Component({
  selector: 'farmbyte-table-colum-action-button',
  standalone: true,
  imports: [
    Button
  ],
  template: `
    <div class="flex flex-row justify-content-end m-0 mr-2">
      <p-button
        icon="pi pi-pencil"
        [rounded]="true"
        severity="info"
        [text]="true"
        size="small"
        (onClick)="onEdit()"/>
      <p-button
        icon="pi pi-trash"
        severity="danger"
        [rounded]="true"
        [text]="true"
        size="small"
        (onClick)="onDelete()"/>
    </div>

  `,
  styles: `
  `,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TableColumActionButtonComponent {
  edit = model<number>(0);
  delete = model<number>(0);

  onEdit() {
    this.edit.set(this.edit() + 1);
  }

  onDelete() {
    this.delete.set(this.delete() + 1);
  }
}
