import {ChangeDetectionStrategy, Component, effect, inject, signal} from '@angular/core';
import {UomStore} from "../../store/uom.store";
import {UomCategoryStore} from "../../../uom-category/store/uom-category.store";
import {FormsModule, NgForm} from "@angular/forms";
import {UomType} from "../../enums/uom-type.enum";
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
import {JsonPipe} from "@angular/common";


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
    ToastModule,
    TranslateModule,
    FormsModule,
    JsonPipe
  ],
  templateUrl: './uom-form-edit.component.html',
  styleUrl: './uom-form-edit.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomFormEditComponent {
  readonly uomStore = inject(UomStore);
  readonly uomCategoryStore = inject(UomCategoryStore);
  formMode: FormMode = FormMode.READ;
  uomTypeOptions = [
    {value: UomType.GREATER, label: 'Plus grand'},
    {value: UomType.LOWER, label: 'Plus petit'},
    {value: UomType.REFERENCE, label: 'Reference'}
  ];

  filteredResults: UomCategoryModel[] = [];

  readonly disableFormEffect = effect(() => {
    if(this.uomStore.loading()) {
      this.formMode = FormMode.READ;
    }
  });

  save(uomForm: NgForm): void {
    const {uomCategoryId, ...uom} = uomForm.value;
    this.uomStore.updateUom({...uom, uomCategoryId: uomCategoryId.id});
  }

  onEdit() {
    this.formMode = FormMode.WRITE;
  }

  onCancel() {

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

  protected readonly FormMode = FormMode;
}
