import {ChangeDetectionStrategy, Component, effect, inject, signal, Signal, viewChild} from '@angular/core';
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
import {NoDataFoundComponent} from "../../../../../shared/components/no-data-found/no-data-found.component";
import {TreeHeaderComponent} from "../../../../../shared/components/tree-header/tree-header.component";
import {TreeSearchComponent} from "../../../../../shared/components/tree-search/tree-search.component";
import {
  TableColumActionButtonComponent
} from "../../../../../shared/components/table-colum-action-button/table-colum-action-button.component";
import {TableColumModel} from "../../../../../core/model/table-colum.model";

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
    TranslateModule,
    NoDataFoundComponent,
    TreeHeaderComponent,
    TreeSearchComponent,
    TableColumActionButtonComponent
  ],
  templateUrl: './product-category-tree.component.html',
  styleUrl: './product-category-tree.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductCategoryTreeComponent {

  readonly productCategoryStore = inject(ProductCategoryStore);
  treeSearch = viewChild.required(TreeSearchComponent);
  pageSizeOptions = signal(DEFAULT_PAGE_SIZE_OPTIONS);
  rows = signal(DEFAULT_SIZE_VALUE);
  first = signal(0);
  columns = signal<TableColumModel[]>([
    {
      name: 'name',
      label: 'product_category_tree_name'
    },
    {
      name: 'Parent',
      label: 'product_category_tree_parent_product_category_id'
    }
  ])
  protected readonly signal = signal;

  constructor() {
    effect(() => {
      this.applyFilter(this.treeSearch().keyword());
    });
  }

  getProductCategoryName(productCategoryId: string): Signal<ProductCategoryModel | undefined> {
    return this.productCategoryStore.findProductCategoryById(productCategoryId);
  }

  onPageChange(event: any): void {
    this.first.set(event.first);
    this.rows.set(event.rows);
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

  applyFilter(keyword: string) {
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
