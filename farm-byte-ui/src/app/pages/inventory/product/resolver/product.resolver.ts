import {ResolveFn} from '@angular/router';
import {inject} from "@angular/core";
import {ProductStore} from "../store/product.store";
import {
  DEFAULT_DIRECTION_VALUE,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../core/constants/page.constant";

export const productsResolver: ResolveFn<any> = (route, state) => {
  return inject(ProductStore).findProducts({
    page: DEFAULT_PAGE_VALUE,
    size: DEFAULT_SIZE_VALUE,
    attribute: "name",
    direction: DEFAULT_DIRECTION_VALUE
  });
};
