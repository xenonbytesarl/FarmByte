import {ResolveFn} from '@angular/router';
import {inject} from "@angular/core";
import {
  DEFAULT_DIRECTION_VALUE,
  DEFAULT_PAGE_VALUE,
  DEFAULT_SIZE_VALUE
} from "../../../../core/constants/page.constant";
import {UomStore} from "../store/uom.store";

export const uomsResolver: ResolveFn<any> = (route, state) => {
  return inject(UomStore).findUoms(
    {
      page: DEFAULT_PAGE_VALUE,
      size: DEFAULT_SIZE_VALUE,
      attribute: "name",
      direction: DEFAULT_DIRECTION_VALUE
    }
  );
};
