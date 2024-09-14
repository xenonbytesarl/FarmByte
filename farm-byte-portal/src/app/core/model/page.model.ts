export interface PageModel<T> {
  first: boolean;
  last: boolean;
  pageSize: number;
  totalPages: number;
  totalElements: number;
  content: Array<T>;
}
