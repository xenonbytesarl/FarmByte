import {ResolveFn} from '@angular/router';
import {inject} from "@angular/core";
import {
  DEFAULT_DIRECTION_VALUE,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../core/constants/page.constant";
import {UomStore} from "../store/uom.store";
import {map, of} from "rxjs";


export const findUomsResolver: ResolveFn<boolean> = (route, state) => {
  inject(UomStore).findUoms(
    {
      page: DEFAULT_PAGE_VALUE,
      size: DEFAULT_SIZE_VALUE,
      attribute: "name",
      direction: DEFAULT_DIRECTION_VALUE
    }
  );
  return of(true);
};

export const findUomByIdResolver: ResolveFn<any> = (route, state) => {
  const uomStore = inject(UomStore);
  const uomId = route.params['uomId'];
  if(uomId) {
    uomStore.findUomById(uomId);
  }
  return of(true)
}
