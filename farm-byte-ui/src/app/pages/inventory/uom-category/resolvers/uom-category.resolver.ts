import {ResolveFn} from '@angular/router';
import {inject} from "@angular/core";
import {UomCategoryStore} from "../store/uom-category.store";
import {
  DEFAULT_DIRECTION_VALUE,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../core/constants/page.constant";
import {of} from "rxjs";

export const findUomCategoriesResolver: ResolveFn<boolean> = (route, state) => {
  inject(UomCategoryStore).findUomCategories(
    {
      page: DEFAULT_PAGE_VALUE,
      size: DEFAULT_SIZE_VALUE,
      attribute: "name",
      direction: DEFAULT_DIRECTION_VALUE
    }
  );
  return of(true);
};


export const findUomCategoryByIdResolver: ResolveFn<boolean> = (route, state) => {
  const uomStore = inject(UomCategoryStore);
  const uomCategoryId = route.params['uomCategoryId'];
  if(uomCategoryId) {
    uomStore.findUomCategoryById(uomCategoryId);
  }
  return of(true);
}
