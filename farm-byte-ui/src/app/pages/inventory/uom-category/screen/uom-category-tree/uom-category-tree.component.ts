import {ChangeDetectionStrategy, Component, effect, inject, signal, viewChild} from '@angular/core';
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
import {NoDataFoundComponent} from "../../../../../shared/components/no-data-found/no-data-found.component";
import {RouterLink} from "@angular/router";
import {TreeHeaderComponent} from "../../../../../shared/components/tree-header/tree-header.component";
import {TreeSearchComponent} from "../../../../../shared/components/tree-search/tree-search.component";

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
    InputTextModule,
    NoDataFoundComponent,
    RouterLink,
    TreeHeaderComponent,
    TreeSearchComponent
  ],
  templateUrl: './uom-category-tree.component.html',
  styleUrl: './uom-category-tree.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomCategoryTreeComponent {
  readonly uomCategoryStore = inject(UomCategoryStore);
  treeSearch = viewChild.required(TreeSearchComponent);
  pageSizeOptions = signal(DEFAULT_PAGE_SIZE_OPTIONS);
  rows = signal(DEFAULT_SIZE_VALUE);
  first = signal(0);
  protected readonly signal = signal;

  constructor() {
    effect(() => {
      this.applyFilter(this.treeSearch().keyword());
    });
  }

  onPageChange(event: any): void {
    this.first.set(event.first);
    this.rows.set(event.rows);
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

  applyFilter(keyword: string) {
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
