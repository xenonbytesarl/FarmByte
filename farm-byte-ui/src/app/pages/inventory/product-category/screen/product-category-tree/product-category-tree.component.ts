import {ChangeDetectionStrategy, Component, computed, inject, Signal} from '@angular/core';
import {ProductCategoryStore} from "../../store/product-category.store";
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
import {TranslateModule} from "@ngx-translate/core";
import {ProductCategoryModel} from "../../model/product-category.model";

@Component({
  selector: 'farmbyte-product-category-tree',
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
    TranslateModule
  ],
  templateUrl: './product-category-tree.component.html',
  styleUrl: './product-category-tree.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductCategoryTreeComponent {
  readonly productCategoryStore = inject(ProductCategoryStore);
  pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  rows = DEFAULT_SIZE_VALUE;
  first = 0;

  getProductCategoryName(productCategoryId: string): Signal<ProductCategoryModel | undefined> {
    return this.productCategoryStore.findProductCategoryById(productCategoryId);
  }

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
    this.productCategoryStore.findProductCategories({
      page: event.page,
      size: event.rows,
      attribute: "name",
      direction: Direction.ASC,
    });
  }

  onListChange(event: any): void {

    this.productCategoryStore.findProductCategories({
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
    this.productCategoryStore.searchProductCategories(searchParamModel);
  }

  edit(id: string) {
    this.productCategoryStore.findProductCategoryById(id);
  }

  delete(productCategory: any) {
    //TODO implement later
  }
}
