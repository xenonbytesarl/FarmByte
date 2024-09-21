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
import {FormHeaderComponent} from "../../../../../shared/components/form-header/form-header.component";

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
    RouterLink,
    FormHeaderComponent
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
    //run to initialize form with value
    effect(() => {
      if(this.isEditionMode()) {
        this.uomCategoryForm.patchValue(this.uomCategoryStore.selectedUomCategory() as UomCategoryModel);
        this.disableControl();
      }
    });

    //run when creating new record
    effect(() => {
      if(!this.uomCategoryStore.loading() && this.uomCategoryStore.selectedUomCategory() === undefined) {
        this.enableAndResetControl();
      }
    });
  }

  save(): void {
    this.disableControl();
    if(this.isCreationMode()) {
      this.uomCategoryStore.createUomCategory({...this.uomCategoryForm.getRawValue() as UomCategoryModel});
    } else {
      this.uomCategoryStore.updateUomCategory({...this.uomCategoryForm.getRawValue() as UomCategoryModel});
    }
  }

  onCancel(): void {
    if(this.isCreationMode()) {
      this.uomCategoryForm.reset();
    } else {
      this.uomCategoryForm.reset();
      this.uomCategoryForm.patchValue(this.uomCategoryStore.selectedUomCategory() as UomCategoryModel);
      this.uomCategoryStore.initForm(FormMode.READ);
      this.disableControl();
    }
  }

  onEdit(): void {
    this.uomCategoryStore.initForm(FormMode.WRITE);
    this.enableControl();
  }

  get name() {
    return this.uomCategoryForm.get('name');
  }

  private disableControl() {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.disable();
      }
    });
  }

  private enableControl() {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
      }
    });
  }

  private enableAndResetControl() {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
        control.reset();
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
