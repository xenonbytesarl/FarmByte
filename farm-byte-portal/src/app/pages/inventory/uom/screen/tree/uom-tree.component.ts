import {
  AfterViewInit,
  ChangeDetectionStrategy,
  Component,
  computed,
  inject,
  Signal,
  signal,
  ViewChild,
  WritableSignal
} from '@angular/core';
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort, MatSortHeader, Sort} from "@angular/material/sort";
import {UomStore} from "../../store/uom.store";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell, MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef, MatTable,
  MatTableDataSource
} from "@angular/material/table";
import {SelectionModel} from "@angular/cdk/collections";
import {DEFAULT_PAGE_SIZE_OPTIONS, DEFAULT_PAGE_VALUE, DEFAULT_SIZE_VALUE} from "../../../../../core/constants/page.constant";
import {UomModel} from "../../model/uom.model";
import {FindParamModel} from "../../../../../core/model/find-param.model";
import {Direction} from "../../../../../core/enums/direction.enum";
import {MatCheckbox} from "@angular/material/checkbox";
import {TranslateModule} from "@ngx-translate/core";
import {DecimalPipe, TitleCasePipe} from "@angular/common";
import {UomCategoryStore} from "../../../uom-category/store/uom-category.store";
import {UomCategoryModel} from "../../../uom-category/model/uom-category.model";
import {MatFormField, MatLabel, MatPrefix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {SearchParamModel} from "../../../../../core/model/search-param.model";
import {MatButton} from "@angular/material/button";
import {MatIcon, MatIconModule} from "@angular/material/icon";

@Component({
  selector: 'farmbyte-unit-of-measures',
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
    MatHeaderCellDef,
    DecimalPipe,
    TitleCasePipe,
    MatFormField,
    MatInput,
    MatLabel,
    MatButton,
    MatIconModule,
    MatPrefix
  ],
  templateUrl: './uom-tree.component.html',
  styleUrl: './uom-tree.component.scss',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UomTreeComponent implements AfterViewInit {

  readonly uomStore = inject(UomStore);
  readonly uomCategoryStore = inject(UomCategoryStore);

  @ViewChild(MatPaginator, { read: true }) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource =
    computed(() => new MatTableDataSource<UomModel>(this.uomStore.uomDataSource()));

  totalElements = computed(() => this.uomStore.uomTotalElements());

  pageSize = computed(() => this.uomStore.uomTotalPageSize());

  selection: WritableSignal<SelectionModel<UomModel>> =
    signal(new SelectionModel<UomModel>(true, []));

  pageEvent: WritableSignal<PageEvent> = signal({pageSize: DEFAULT_SIZE_VALUE, pageIndex: DEFAULT_PAGE_VALUE, length: this.totalElements() as number});
  sortEvent: WritableSignal<Sort> = signal({active: 'name', direction: 'asc'});

  displayedColumns: string[] = ['select', 'name', 'uomCategoryId', 'uomType', 'ratio'];
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
  checkboxLabel(row?: UomModel): string {
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
    this.uomStore.findUoms(findParamModel);
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
    this.uomStore.findUoms(findParamModel);
  }

  getUomCategoryName(uomCategoryId: string): Signal<UomCategoryModel | undefined> {
    return this.uomCategoryStore.findUomCategoryById(uomCategoryId);
  }

  applyFilter(event: KeyboardEvent) {
    const keyword = (event.target as HTMLInputElement).value;
    const searchParamModel: SearchParamModel = {
      page: this.pageEvent().pageIndex,
      size: this.pageEvent().pageSize,
      attribute: this.sortEvent().active,
      direction: this.sortEvent().direction === "asc"? Direction.ASC: Direction.DSC,
      keyword: keyword
    };
    this.uomStore.searchUoms(searchParamModel);
  }
}
