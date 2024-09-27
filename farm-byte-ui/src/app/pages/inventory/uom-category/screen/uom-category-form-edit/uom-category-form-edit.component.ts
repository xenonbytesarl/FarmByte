import {Component, effect, inject} from '@angular/core';
import {CardModule} from "primeng/card";
import {FormHeaderComponent} from "../../../../../shared/components/form-header/form-header.component";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ToastModule} from "primeng/toast";
import {TranslateModule} from "@ngx-translate/core";
import {UomCategoryStore} from "../../store/uom-category.store";
import {FormMode} from "../../../../../core/enums/form-mode.enum";
import {UomCategoryModel} from "../../model/uom-category.model";
import {ActivatedRoute} from "@angular/router";


@Component({
  selector: 'farmbyte-uom-category-form-edit',
  standalone: true,
    imports: [
        CardModule,
        FormHeaderComponent,
        InputTextModule,
        PaginatorModule,
        ReactiveFormsModule,
        ToastModule,
        TranslateModule
    ],
  templateUrl: './uom-category-form-edit.component.html',
  styleUrl: './uom-category-form-edit.component.scss'
})
export class UomCategoryFormEditComponent {
  readonly uomCategoryStore = inject(UomCategoryStore);
  readonly fb = inject(FormBuilder);
  readonly activatedRoute = inject(ActivatedRoute);
  formMode: FormMode = FormMode.READ;
  uomCategoryForm: FormGroup = this.fb.nonNullable.group({
    id: [''],
    name: ['', [Validators.required]],
    parentUomCategoryId: [''],
    active: [false]
  });

  readonly setUomCategoryForm = effect(() => {
    const uomCategoryLoaded = this.uomCategoryStore.selectedUomCategory();
    if(uomCategoryLoaded) {
      this.uomCategoryForm.patchValue(uomCategoryLoaded);
      this.disableControl();
    }
  });

  readonly disableControlEffect = effect(() => {
    //run when form is loading
    if(this.uomCategoryStore.loading() || (!this.uomCategoryStore.loading() && this.uomCategoryStore.error() === undefined)) {
      this.disableControl();
    }
  });

  readonly enableControlEffect = effect(() => {
    //run when form is not loading and there is an error
    if(!this.uomCategoryStore.loading() && this.uomCategoryStore.error() !== undefined) {
      this.enableControl();
    }
  });


  save(): void {
    this.uomCategoryStore.updateUomCategory({
      ...this.uomCategoryForm.getRawValue() as UomCategoryModel
    });
  }

  onEdit() {
    this.enableControl();
  }

  onCancel() {
    this.resetControl();
    this.disableControl();
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
    this.formMode = FormMode.READ;
  }

  private enableControl() {
    Object.keys(this.uomCategoryForm.controls).forEach((key: string) => {
      const control = this.uomCategoryForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
      }
    });
    this.formMode = FormMode.WRITE;
  }

  private resetControl() {
    this.uomCategoryForm.patchValue(this.uomCategoryStore.selectedUomCategory() as UomCategoryModel);
    this.formMode = FormMode.WRITE;
  }
}
