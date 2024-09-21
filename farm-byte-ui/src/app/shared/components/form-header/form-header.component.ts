import {Component, input, model} from '@angular/core';
import {FormMode} from "../../../core/enums/form-mode.enum";
import {Button} from "primeng/button";
import {RouterLink} from "@angular/router";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-form-header',
  standalone: true,
  imports: [
    Button,
    RouterLink,
    TranslateModule
  ],
  templateUrl: './form-header.component.html',
  styleUrl: './form-header.component.scss'
})
export class FormHeaderComponent {

  protected readonly FormMode = FormMode;
  formMode = input<FormMode>();
  loading = input<boolean>();
  disabledSaveBtn = input<boolean>(false);
  newRecordLink = input<string>('');
  formTitle = input<string>('');
  edit = model<number>(0);
  cancel = model<number>(0);


  onCancel() {
    this.cancel.set(this.cancel() + 1);
  }

  onEdit() {
    this.edit.set(this.edit() + 1);
  }
}
