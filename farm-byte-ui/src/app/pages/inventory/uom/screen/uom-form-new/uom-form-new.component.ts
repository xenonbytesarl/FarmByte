import {
  ChangeDetectionStrategy,
  Component,
  computed,
  effect,
  ElementRef,
  inject,
  viewChild,
  ViewChild
} from '@angular/core';
import {UomStore} from "../../store/uom.store";
import {FormsModule, NgForm, ReactiveFormsModule} from "@angular/forms";
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
import {AutocompleteEventModel} from "../../../../../core/model/autocomplete-event.model";
import {FormMode} from "../../../../../core/enums/form-mode.enum";
import {JsonPipe} from "@angular/common";
import {UomTreeComponent} from "../uom-tree/uom-tree.component";
import {concat, concatMap, delayWhen, finalize, flatMap, interval, map, of, switchMap, takeWhile, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {UomCategoryModel} from "../../../uom-category/model/uom-category.model";

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
    CheckboxModule,
    JsonPipe
  ],
  templateUrl: './uom-form-new.component.html',
  styleUrl: './uom-form-new.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomFormNewComponent {

  uomForm = viewChild<NgForm>('uomForm');

  readonly uomStore = inject(UomStore);
  readonly uomCategoryStore = inject(UomCategoryStore);
  filteredResults: any[] = [];
  formMode: FormMode = FormMode.WRITE;

  uomTypeOptions = [
    {value: UomType.GREATER, label: 'Plus grand'},
    {value: UomType.LOWER, label: 'Plus petit'},
    {value: UomType.REFERENCE, label: 'Reference'}
  ];

  save(): void {
    //this.uomStore.createUom({...this.uomForm()?.value})

    of(true)
      .pipe(
        switchMap(() => {
          return of(this.uomStore.createUom({...this.uomForm()?.value}))
            .pipe(
              tapResponse({
                next: (result: any) => {
                  console.log('Reset The form', result);
                  this.uomForm()?.resetForm();
                },
                error: (error) => {}
              })
            )
        })
      ).subscribe(() => console.log('Reset the form terminé'));
  }

  onCancel() {
    this.uomForm()?.resetForm();
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
}
