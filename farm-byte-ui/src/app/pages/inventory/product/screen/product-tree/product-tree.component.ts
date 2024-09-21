import {Component, effect, inject, signal, viewChild} from '@angular/core';
import {ProductStore} from "../../store/product.store";
import {
  DEFAULT_PAGE_SIZE_OPTIONS,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../../core/constants/page.constant";
import {Direction} from "../../../../../core/enums/direction.enum";
import {SearchParamModel} from "../../../../../core/model/search-param.model";
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {PrimeTemplate} from "primeng/api";
import {TableModule} from "primeng/table";
import {TitleCasePipe} from "@angular/common";
import {TranslateModule} from "@ngx-translate/core";
import {TreeHeaderComponent} from "../../../../../shared/components/tree-header/tree-header.component";
import {TreeSearchComponent} from "../../../../../shared/components/tree-search/tree-search.component";
import {NoDataFoundComponent} from "../../../../../shared/components/no-data-found/no-data-found.component";
import {
  TableColumActionButtonComponent
} from "../../../../../shared/components/table-colum-action-button/table-colum-action-button.component";

@Component({
  selector: 'farmbyte-product-tree',
  standalone: true,
  imports: [
    Button,
    DropdownModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    PaginatorModule,
    PrimeTemplate,
    TableModule,
    TitleCasePipe,
    TranslateModule,
    TreeHeaderComponent,
    TreeSearchComponent,
    NoDataFoundComponent,
    TableColumActionButtonComponent
  ],
  templateUrl: './product-tree.component.html',
  styleUrl: './product-tree.component.scss'
})
export class ProductTreeComponent {
  readonly productStore = inject(ProductStore);
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
    this.productStore.findProducts({
      page: event.page,
      size: event.rows,
      attribute: "name",
      direction: Direction.ASC,
    });
  }

  onListChange(event: any): void {

    this.productStore.findProducts({
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
    this.productStore.searchProducts(searchParamModel);
  }

  edit(id: string) {
    this.productStore.findProductById(id);
  }

  delete(product: any) {
    //TODO implement later
  }
}
