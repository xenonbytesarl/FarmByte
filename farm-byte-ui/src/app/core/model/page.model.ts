export interface PageModel<T> {
  first?: boolean;
  last?: boolean;
  pageSize?: number;
  totalPages?: number;
  totalElements?: number;
  elements: Array<T>;
}
