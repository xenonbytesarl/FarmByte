import {ChangeDetectionStrategy, Component, inject} from '@angular/core';
import {TableModule} from "primeng/table";
import {PaginatorModule} from "primeng/paginator";
import {DropdownModule} from "primeng/dropdown";
import {AutoCompleteModule} from "primeng/autocomplete";
import {UomCategoryStore} from "../../store/uom-category.store";
import {TranslateModule} from "@ngx-translate/core";
import {
  DEFAULT_PAGE_SIZE_OPTIONS,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../../core/constants/page.constant";
import {Direction} from "../../../../../core/enums/direction.enum";
import {Button} from "primeng/button";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {SearchParamModel} from "../../../../../core/model/search-param.model";

@Component({
  selector: 'farmbyte-uom-category-tree',
  standalone: true,
  imports: [
    TableModule,
    PaginatorModule,
    DropdownModule,
    AutoCompleteModule,
    TranslateModule,
    Button,
    IconFieldModule,
    InputIconModule,
    InputTextModule
  ],
  templateUrl: './uom-category-tree.component.html',
  styleUrl: './uom-category-tree.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomCategoryTreeComponent {
  readonly uomCategoryStore = inject(UomCategoryStore);
  pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  rows = DEFAULT_SIZE_VALUE;
  first = 0;

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.uomCategoryStore.findUomCategories({
      page: event.page,
      size: event.rows,
      attribute: "name",
      direction: Direction.ASC,
    });
  }

  onListChange(event: any): void {

    this.uomCategoryStore.findUomCategories({
      page: DEFAULT_PAGE_VALUE,
      size: event.value,
      attribute: "name",
      direction: Direction.ASC,
    });
  }

  applyFilter(event: KeyboardEvent) {
    const keyword = (event.target as HTMLInputElement).value;
    const searchParamModel: SearchParamModel = {
      page: DEFAULT_PAGE_VALUE,
      size: DEFAULT_SIZE_VALUE,
      attribute: "name",
      direction: Direction.ASC,
      keyword: keyword
    };
    this.uomCategoryStore.searchUomCategories(searchParamModel);
  }

  edit(id: string) {
    this.uomCategoryStore.findUomCategoryById(id);
  }

  delete(uomCategory: any) {
    //TODO implement later
  }
}
