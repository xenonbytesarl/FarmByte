import { ResolveFn } from '@angular/router';
import {inject} from "@angular/core";
import {UomCategoryStore} from "../store/uom-category.store";
import {
  DEFAULT_DIRECTION_VALUE,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../core/constants/page.constant";

export const uomCategoriesResolver: ResolveFn<any> = (route, state) => {
  return inject(UomCategoryStore).findUomCategories(
    {
      page: DEFAULT_PAGE_VALUE,
      size: DEFAULT_SIZE_VALUE,
      attribute: "name",
      direction: DEFAULT_DIRECTION_VALUE
    }
  );
};
