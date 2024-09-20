import {ChangeDetectionStrategy, Component, effect, inject} from '@angular/core';
import {ChipsModule} from "primeng/chips";
import {FloatLabelModule} from "primeng/floatlabel";
import {TranslateModule} from "@ngx-translate/core";
import {FormBuilder, FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {Button} from "primeng/button";
import {CardModule} from "primeng/card";
import {UomCategoryStore} from "../../store/uom-category.store";
import {UomCategoryModel} from "../../model/uom-category.model";
import {ToastModule} from "primeng/toast";
import {FormMode} from "../../../../../core/enums/form-mode.enum";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'farmbyte-uom-category-form',
  standalone: true,
  imports: [
    ChipsModule,
    FloatLabelModule,
    TranslateModule,
    ReactiveFormsModule,
    Button,
    CardModule,
    ToastModule,
    RouterLink
  ],
  templateUrl: './uom-category-form.component.html',
  styleUrl: './uom-category-form.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomCategoryFormComponent {

  uomCategoryStore = inject(UomCategoryStore);
  protected readonly FormMode = FormMode;
  uomCategoryForm = inject(FormBuilder).group({
    id: [''],
    name: ['', [Validators.required]],
    parentUomCategoryId: [''],
    active: [false]
  });

  constructor() {
    effect(() => {
      if(this.isEditionMode()) {
        this.uomCategoryForm.patchValue(this.uomCategoryStore.selectedUomCategory() as UomCategoryModel);
        this.disableForm();
      }
    });
  }

  save(): void {
    this.disableForm();
    if(this.isCreationMode()) {
      this.uomCategoryStore.createUomCategory({...this.uomCategoryForm.getRawValue() as UomCategoryModel});
    } else {
      this.uomCategoryStore.updateUomCategory({...this.uomCategoryForm.getRawValue() as UomCategoryModel});
    }

  }

  cancel(): void {
    if(this.isCreationMode()) {
      this.uomCategoryForm.reset();
    } else {
      this.uomCategoryForm.reset();
      this.uomCategoryForm.patchValue(this.uomCategoryStore.selectedUomCategory() as UomCategoryModel);
      this.uomCategoryStore.initForm(FormMode.READ);
      this.disableForm();
    }
  }

  edit(): void {
    this.uomCategoryStore.initForm(FormMode.WRITE);
    this.enableForm();
  }

  get name() {
    return this.uomCategoryForm.get('name');
  }

  private disableForm() {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.disable();
      }
    });
  }

  private enableForm() {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
      }
    });
  }

  private isCreationMode(): boolean {
    return this.uomCategoryStore.selectedUomCategory() === undefined
      && this.uomCategoryStore.formMode() === FormMode.WRITE;
  }

  private isEditionMode(): boolean {
    return this.uomCategoryStore.selectedUomCategory() !== undefined
      && this.uomCategoryStore.formMode() === FormMode.READ;
  }
}
