import {ChangeDetectionStrategy, Component, effect, inject, OnInit} from '@angular/core';
import {UomStore} from "../../store/uom.store";
import {FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {CardModule} from "primeng/card";
import {ToastModule} from "primeng/toast";
import {FormHeaderComponent} from "../../../../../shared/components/form-header/form-header.component";
import {InputTextModule} from "primeng/inputtext";
import {TranslateModule} from "@ngx-translate/core";
import {AutoCompleteModule} from "primeng/autocomplete";
import {UomCategoryStore} from "../../../uom-category/store/uom-category.store";
import {FloatLabelModule} from "primeng/floatlabel";
import {DropdownModule} from "primeng/dropdown";
import {UomType} from "../../enums/uom-type.enum";
import {InputNumberModule} from "primeng/inputnumber";
import {CheckboxModule} from "primeng/checkbox";
import {UomModel} from "../../model/uom.model";
import {AutocompleteEventModel} from "../../../../../core/model/autocomplete-event.model";
import {FormMode} from "../../../../../core/enums/form-mode.enum";

@Component({
  selector: 'farmbyte-uom-form',
  standalone: true,
  imports: [
    CardModule,
    ToastModule,
    FormHeaderComponent,
    FormsModule,
    ReactiveFormsModule,
    InputTextModule,
    TranslateModule,
    AutoCompleteModule,
    FloatLabelModule,
    DropdownModule,
    InputNumberModule,
    CheckboxModule
  ],
  templateUrl: './uom-form-new.component.html',
  styleUrl: './uom-form-new.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomFormNewComponent implements OnInit{
  readonly uomStore = inject(UomStore);
  readonly uomCategoryStore = inject(UomCategoryStore);
  readonly fb = inject(FormBuilder);
  filteredResults: any[] = [];
  uomForm!: FormGroup;
  formMode: FormMode = FormMode.WRITE;

  uomTypeOptions = [
    {value: UomType.GREATER, label: 'Plus grand'},
    {value: UomType.LOWER, label: 'Plus petit'},
    {value: UomType.REFERENCE, label: 'Reference'}
  ];

  constructor() {
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
      if(!this.uomStore.loading() && this.uomStore.error() === undefined) {
        this.resetControl();
      }
    }, );
  }

  ngOnInit() {
    this.initForm();
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
    this.uomStore.createUom({...this.uomForm.getRawValue() as UomModel});
  }

  onCancel() {
    this.resetControl();
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

  filterUomCategory(event: AutocompleteEventModel) {
    let filtered: any[] = [];
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
  }

  private enableControl() {

    Object.keys(this.uomForm.controls).forEach((key: string) => {
      const control = this.uomForm.get(key);
      if(control && control instanceof FormControl) {
        control.enable();
      }
    });
  }

  private resetControl() {
    this.initForm();
  }

}
