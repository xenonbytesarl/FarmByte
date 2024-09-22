import {Component, effect, inject, OnInit} from '@angular/core';
import {UomStore} from "../../store/uom.store";
import {UomCategoryStore} from "../../../uom-category/store/uom-category.store";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {UomType} from "../../enums/uom-type.enum";
import {UomModel} from "../../model/uom.model";
import {AutocompleteEventModel} from "../../../../../core/model/autocomplete-event.model";
import {AutoCompleteModule} from "primeng/autocomplete";
import {CardModule} from "primeng/card";
import {CheckboxModule} from "primeng/checkbox";
import {DropdownModule} from "primeng/dropdown";
import {FormHeaderComponent} from "../../../../../shared/components/form-header/form-header.component";
import {InputNumberModule} from "primeng/inputnumber";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {ToastModule} from "primeng/toast";
import {TranslateModule} from "@ngx-translate/core";
import {FormMode} from "../../../../../core/enums/form-mode.enum";
import {UomCategoryModel} from "../../../uom-category/model/uom-category.model";

@Component({
  selector: 'farmbyte-uom-form-edit',
  standalone: true,
  imports: [
    AutoCompleteModule,
    CardModule,
    CheckboxModule,
    DropdownModule,
    FormHeaderComponent,
    InputNumberModule,
    InputTextModule,
    PaginatorModule,
    ReactiveFormsModule,
    ToastModule,
    TranslateModule
  ],
  templateUrl: './uom-form-edit.component.html',
  styleUrl: './uom-form-edit.component.scss'
})
export class UomFormEditComponent implements OnInit {
  readonly uomStore = inject(UomStore);
  readonly uomCategoryStore = inject(UomCategoryStore);
  readonly fb = inject(FormBuilder);
  filteredResults: UomCategoryModel[] = [];
  uomForm!: FormGroup;
  formMode: FormMode = FormMode.READ;

  uomTypeOptions = [
    {value: UomType.GREATER, label: 'Plus grand'},
    {value: UomType.LOWER, label: 'Plus petit'},
    {value: UomType.REFERENCE, label: 'Reference'}
  ];

  constructor() {
    this.initForm();
    effect(() => {
      //run when form is loading
      if(this.uomStore.loading()) {
        this.disableControl();
      }
      //run when form is not loading and there is an error
      if(!this.uomStore.loading() && this.uomStore.error() !== undefined) {
        this.enableControl();
      }
      //run when form is not loading and there is no error
      if(!this.uomStore.loading() && this.uomStore.error === undefined) {
        this.disableControl();
      }
    }, );
  }

  ngOnInit() {
    if(this.uomStore.selectedUom() !== undefined) {
      this.uomForm.patchValue(this.uomToUomFormValue(this.uomStore.selectedUom() as UomModel));
      this.disableControl();
    }
  }

  initForm(): void {
    this.uomForm = this.fb.nonNullable.group({
      id: [''],
      name: ['', [Validators.required]],
      uomCategoryId: ['', [Validators.required]],
      uomType: ['', [Validators.required]],
      ratio: [0],
      active: [false]
    })
  }

  save(): void {
    this.uomStore.updateUom({
      ...this.uomForm.getRawValue() as UomModel,
      uomCategoryId: this.uomForm.get('uomCategoryId')?.value.id
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
    return this.uomForm.get('name');
  }

  get uomCategoryId() {
    return this.uomForm.get('uomCategoryId');
  }

  get uomType() {
    return this.uomForm.get('uomType');
  }

  filterUomCategory(event: AutocompleteEventModel): void {
    let filtered: UomCategoryModel[] = [];
    let query = event.query;

    for (let i = 0; i < (this.uomCategoryStore.uomCategoryEntities()).length; i++) {
      let uomCategory = (this.uomCategoryStore.uomCategoryEntities())[i];
      if (uomCategory.name.toLowerCase().indexOf(query.toLowerCase()) == 0) {
        filtered.push(uomCategory);
      }
    }

    this.filteredResults = filtered;
  }

  private disableControl() {
    Object.keys(this.uomForm.controls).forEach((key: string) => {
      const control = this.uomForm.get(key);
      if(control && control instanceof FormControl) {
        control.disable();
      }
    });
    this.formMode = FormMode.READ;
  }

  private enableControl() {
    Object.keys(this.uomForm.controls).forEach((key: string) => {
      const control = this.uomForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
      }
    });
    this.formMode = FormMode.WRITE;
  }

  private resetControl() {
    this.initForm();
    this.uomForm.patchValue(this.uomToUomFormValue(this.uomStore.selectedUom() as UomModel));
    this.formMode = FormMode.WRITE;
  }


  private uomToUomFormValue(selectedUom: UomModel) {
   return {
      id: selectedUom.id,
      name: selectedUom.name,
      uomCategoryId: this.uomStore.findUomCategoryById(selectedUom.uomCategoryId)(),
      uomType: selectedUom.uomType,
      ratio: selectedUom.ratio,
      active: selectedUom.active
    };
  }
}
