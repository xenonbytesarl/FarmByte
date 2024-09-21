import {signalStoreFeature, withState} from "@ngrx/signals";

type PageState = {
  pageSize: number;
  totalElements: number;
}

function withPageStatus() {
  return signalStoreFeature(withState<PageState>({pageSize: 0, totalElements: 0}));
}

function setPageSize(value: number): Partial<PageState> {
  return {pageSize: value};
}

function setTotalElements(value: number): Partial<PageState> {
  return {totalElements: value};
}

export {withPageStatus, setPageSize, setTotalElements};
