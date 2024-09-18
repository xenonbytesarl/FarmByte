import {Component, inject} from '@angular/core';
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
    TranslateModule
  ],
  templateUrl: './product-tree.component.html',
  styleUrl: './product-tree.component.scss'
})
export class ProductTreeComponent {
  readonly productStore = inject(ProductStore);
  pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;
  rows = DEFAULT_SIZE_VALUE;
  first = 0;

  onPageChange(event: any): void {
    this.first = event.first;
    this.rows = event.rows;
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

  applyFilter(event: KeyboardEvent) {
    const keyword = (event.target as HTMLInputElement).value;
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
