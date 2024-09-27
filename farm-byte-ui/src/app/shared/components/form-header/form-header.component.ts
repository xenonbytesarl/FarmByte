import {ChangeDetectionStrategy, Component, input, model} from '@angular/core';
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
  template:`
    <div class="flex flex-row justify-content-between align-items-center w-full">
      <p class="text-2xl w-3/12">{{formTitle() | translate}}</p>
      <div class="flex flex-row justify-content-end align-items-center gap-3 w-9/12">
        @if(formMode() === FormMode.WRITE) {
          <p-button type="submit" [icon]="loading()? 'pi pi-spin pi-spinner': 'pi pi-check'"  size="small"
                    class="font-normal mr-1" [disabled]="disabledSaveBtn()" [rounded]="true"
                    label="{{'form_button_label_save' | translate}}"/>
          <p-button type="button" (click)="onCancel()" icon="pi pi-times" size="small" severity="secondary"
                    [rounded]="true" label="{{'form_button_label_cancel' | translate}}"/>
        } @else {
          <p-button type="button" icon="pi pi-plus" [routerLink]="newRecordLink()" size="small" class="mr-1"
                    [rounded]="true" label="{{'form_button_label_create' | translate}}"/>
          <p-button type="button" (click)="onEdit()" icon="pi pi-pencil" size="small" severity="secondary"
                    class="mr-1" [rounded]="true" label="{{'form_button_label_edit' | translate}}"/>
        }
      </div>
    </div>
  `,
  styles: ``,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FormHeaderComponent {

  protected readonly FormMode = FormMode;
  formMode = input<FormMode>();
  loading = input<boolean>();
  disabledSaveBtn = input<boolean | null>(false);
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
