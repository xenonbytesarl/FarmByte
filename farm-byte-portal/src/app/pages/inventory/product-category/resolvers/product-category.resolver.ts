import { ResolveFn } from '@angular/router';
import {inject} from "@angular/core";
import {ProductCategoryStore} from "../store/product-category.store";
import {DEFAULT_PAGE_VALUE, DEFAULT_SIZE_VALUE} from "../../../../core/constants/page.constant";
import {Direction} from "../../../../core/enums/direction.enum";

export const productCategoriesResolver: ResolveFn<any> = (route, state) => {
  return inject(ProductCategoryStore).findProductCategories(
    {
      page: DEFAULT_PAGE_VALUE,
      size:DEFAULT_SIZE_VALUE,
      attribute: "name",
      direction: Direction.ASC
    }
  );
};
