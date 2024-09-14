import {Component, inject, OnInit} from '@angular/core';
import {UomCategoryStore} from "../store/uom-category.store";
import {Direction} from "../../../../core/enums/direction.enum";
import {Router} from "@angular/router";

@Component({
  selector: 'farmbyte-uom-category-tree',
  standalone: true,
  imports: [],
  templateUrl: './uom-category-tree.component.html',
  styleUrl: './uom-category-tree.component.scss',
  providers: [UomCategoryStore],
})
export class UomCategoryTreeComponent implements OnInit{
  readonly uomCategoryStore = inject(UomCategoryStore);
ngOnInit() {
  this.uomCategoryStore.findUomCategories({page:0, size:2, attribute:'name', direction: Direction.ASC});
}
}
