import {ChangeDetectionStrategy, Component, effect, inject, OnDestroy, OnInit} from '@angular/core';
import {ChipsModule} from "primeng/chips";
import {FloatLabelModule} from "primeng/floatlabel";
import {TranslateModule} from "@ngx-translate/core";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
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
  templateUrl: './uom-category-form-new.component.html',
  styleUrl: './uom-category-form-new.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomCategoryFormNewComponent implements OnDestroy, OnInit{

  readonly uomCategoryStore = inject(UomCategoryStore);
  readonly fb = inject(FormBuilder);
  formMode: FormMode = FormMode.WRITE;
  uomCategoryForm!: FormGroup;

  readonly disableControlEffect = effect(() => {
    //run when form is loading
    if(this.uomCategoryStore.loading()) {
      this.disableControl();
    }
  });

  readonly enableControlEffect = effect(() => {
    //run when form is not loading and there is an error
    if(!this.uomCategoryStore.loading() && this.uomCategoryStore.error() !== undefined) {
      this.enableControl();
    }
  });

  readonly resetControlEffect =  effect(() => {
    //run when form is not loading and there is no error
    if(!this.uomCategoryStore.loading() && this.uomCategoryStore.error() === undefined) {
      this.resetControl();
    }
  });

  ngOnInit(): void {
    this.initForm();
  }

  private initForm() {
    this.uomCategoryForm = this.fb.nonNullable.group({
      id: [''],
      name: ['', [Validators.required]],
      parentUomCategoryId: [''],
      active: [false]
    });
  }

  save(): void {
    this.uomCategoryStore.createUomCategory({
      ...this.uomCategoryForm.getRawValue() as UomCategoryModel
    });
  }

  onCancel(): void {
    this.resetControl();
  }

  get name() {
    return this.uomCategoryForm.get('name');
  }

  private disableControl(): void {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.disable();
      }
    });
  }

  private enableControl(): void {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
      }
    });
  }

  private resetControl() {
    this.initForm();
  }

  ngOnDestroy(): void {
    this.uomCategoryForm.reset();
  }
}
