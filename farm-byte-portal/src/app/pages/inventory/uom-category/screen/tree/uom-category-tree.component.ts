import {AfterViewInit, Component, computed, inject, signal, ViewChild, WritableSignal} from '@angular/core';
import {UomCategoryStore} from "../../store/uom-category.store";
import {MatTableDataSource, MatTableModule} from "@angular/material/table";
import {UomCategoryModel} from "../../model/uom-category.model";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {SelectionModel} from "@angular/cdk/collections";
import {ActivatedRoute} from "@angular/router";
import {MatPaginator, MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {MatSort, MatSortModule, Sort} from "@angular/material/sort";
import {Direction} from "../../../../../core/enums/direction.enum";
import {
  DEFAULT_PAGE_SIZE_OPTIONS,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../../core/constants/page.constant";
import {FindParamModel} from "../../../../../core/model/find-param.model";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'farmbyte-uom-category-tree',
  standalone: true,
  imports: [
    MatTableModule, MatCheckboxModule, MatPaginatorModule, MatSortModule, TranslateModule
  ],
  templateUrl: './uom-category-tree.component.html',
  styleUrl: './uom-category-tree.component.scss'
})
export class UomCategoryTreeComponent implements AfterViewInit {
  readonly uomCategoryStore = inject(UomCategoryStore);

  @ViewChild(MatPaginator, { read: true }) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;


  dataSource =
    computed(() => new MatTableDataSource<UomCategoryModel>(this.uomCategoryStore.uomCategoryDataSource()));

  totalElements = computed(() => this.uomCategoryStore.uomCategoryTotalElements());

  pageSize = computed(() => this.uomCategoryStore.uomCategoryTotalPageSize());

  selection: WritableSignal<SelectionModel<UomCategoryModel>> =
    signal(new SelectionModel<UomCategoryModel>(true, []));

  pageEvent: WritableSignal<PageEvent> = signal({pageSize: DEFAULT_SIZE_VALUE, pageIndex: DEFAULT_PAGE_VALUE, length: this.totalElements() as number});
  sortEvent: WritableSignal<Sort> = signal({active: 'name', direction: 'asc'});

  displayedColumns: string[] = ['select', 'name'];
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
  checkboxLabel(row?: UomCategoryModel): string {
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
    this.uomCategoryStore.findUomCategories(findParamModel);
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
    this.uomCategoryStore.findUomCategories(findParamModel);
  }
}
