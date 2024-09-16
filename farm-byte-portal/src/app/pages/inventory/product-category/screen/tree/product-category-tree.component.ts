import {ChangeDetectionStrategy, Component, computed, inject, signal, ViewChild, WritableSignal} from '@angular/core';
import {ProductCategoryStore} from "../../store/product-category.store";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort, MatSortHeader, Sort} from "@angular/material/sort";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {ProductCategoryModel} from "../../model/product-category.model";
import {SelectionModel} from "@angular/cdk/collections";
import {
  DEFAULT_PAGE_SIZE_OPTIONS,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../../core/constants/page.constant";
import {FindParamModel} from "../../../../../core/model/find-param.model";
import {Direction} from "../../../../../core/enums/direction.enum";
import {MatCheckbox} from "@angular/material/checkbox";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-product-categories',
  standalone: true,
  imports: [
    MatCell,
    MatCellDef,
    MatCheckbox,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSort,
    MatSortHeader,
    MatTable,
    TranslateModule,
    MatHeaderCellDef
  ],
  templateUrl: './product-category-tree.component.html',
  styleUrl: './product-category-tree.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ProductCategoryTreeComponent {
  readonly productCategoryStore = inject(ProductCategoryStore);

  @ViewChild(MatPaginator, { read: true }) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  dataSource =
    computed(() => new MatTableDataSource<ProductCategoryModel>(this.productCategoryStore.productCategoryDataSource()));

  totalElements = computed(() => this.productCategoryStore.productCategoryTotalElements());

  pageSize = computed(() => this.productCategoryStore.productCategoryTotalPageSize());

  selection: WritableSignal<SelectionModel<ProductCategoryModel>> =
    signal(new SelectionModel<ProductCategoryModel>(true, []));

  pageEvent: WritableSignal<PageEvent> = signal({pageSize: DEFAULT_SIZE_VALUE, pageIndex: DEFAULT_PAGE_VALUE, length: this.totalElements() as number});
  sortEvent: WritableSignal<Sort> = signal({active: 'name', direction: 'asc'});

  displayedColumns: string[] = ['select', 'name', 'parentProductCategoryId'];
  pageSizeOptions = DEFAULT_PAGE_SIZE_OPTIONS;

  ngAfterViewInit() {
    this.dataSource().paginator = this.paginator;
    this.dataSource().sort = this.sort;
  }


  /** Whether the number of selected elements matches the total number of rows. */
  isAllSelected(): boolean {
    const numSelected = this.selection().selected.length;
    const numRows = this.dataSource().data.length;
    return numSelected === numRows;
  }

  toggleAllRows(): void {
    if (this.isAllSelected()) {
      this.selection().clear();
      return;
    }

    this.selection().select(...this.dataSource().data);
  }

  /** The label for the checkbox on the passed row */
  checkboxLabel(row?: ProductCategoryModel): string {
    if (!row) {
      return `${this.isAllSelected() ? 'deselect' : 'select'} all`;
    }
    return `${this.selection().isSelected(row) ? 'deselect' : 'select'} row ${1}`;
  }



  handlePageEvent(pageEvent: PageEvent): void {
    this.pageEvent.set(pageEvent);
    const findParamModel: FindParamModel = {
      page: this.pageEvent().pageIndex,
      size: this.pageEvent().pageSize,
      attribute: this.sortEvent().active,
      direction: this.sortEvent().direction === "asc"? Direction.DSC: Direction.ASC
    };
    this.productCategoryStore.findProductCategories(findParamModel);
  }

  sortData(sort: Sort): void {
    if (!sort.active || sort.direction === '') {
      return;
    }
    this.sortEvent.set(sort);
    const findParamModel: FindParamModel = {
      page: this.pageEvent().pageIndex,
      size: this.pageEvent().pageSize,
      attribute: this.sortEvent().active,
      direction: this.sortEvent().direction === "asc"? Direction.DSC: Direction.ASC
    };
    this.productCategoryStore.findProductCategories(findParamModel);
  }
}
