import {patchState, signalStore, type, withMethods, withState} from "@ngrx/signals";
import {ProductCategoryModel} from "../model/product-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {inject, signal, WritableSignal} from "@angular/core";
import {ProductCategoryService} from "../service/product-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {debounceTime, distinctUntilChanged, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {withDevtools} from "@angular-architects/ngrx-toolkit";
import {SearchParamModel} from "../../../../core/model/search-param.model";
import {setAllEntities, withEntities} from "@ngrx/signals/entities";
import {DEBOUNCE_TIMEOUT} from "../../../../core/constants/app.constant";

type ProductCategoryState = {
  pageSize: number;
  totalElements: number;
  loading: boolean;
  error: any;
};

const productCategoryInitialState: ProductCategoryState = {
  pageSize: 0,
  totalElements: 0,
  loading: false,
  error: null
};


export const ProductCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(productCategoryInitialState),
  withEntities({ entity: type<ProductCategoryModel>(), collection: 'productCategory' }),
  withDevtools('productCategoryState'),
  withMethods((store, productCategoryService = inject(ProductCategoryService)) => ({
    findProductCategories: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          productCategoryService.findProductCategories$(findParamModel)
            .pipe(
              tapResponse({
                next: (productCategorySuccessResponse: SuccessResponseModel<PageModel<ProductCategoryModel>>) => {
                  patchState(
                    store,
                    setAllEntities(productCategorySuccessResponse.data.content.elements, {collection: 'productCategory'}),
                    (state) => ({
                    ...state,
                      pageSize: productCategorySuccessResponse.data.content.pageSize,
                      totalElements: productCategorySuccessResponse.data.content.totalElements
                  }))
                },
                error: (error) => {
                  patchState(store, state => ({...state, error}));
                },
                finalize: () => patchState(store, (state) => ({...state, loading: false}))
              })
            )
        )
      )
    ),
    searchProductCategories: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(DEBOUNCE_TIMEOUT),
        distinctUntilChanged(),
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((searchParamModel: SearchParamModel) =>
          productCategoryService.searchProductCategories$(searchParamModel)
            .pipe(
              tapResponse({
                next: (productCategorySuccessResponse: SuccessResponseModel<PageModel<ProductCategoryModel>>) => {
                  patchState(
                    store,
                    setAllEntities(productCategorySuccessResponse.data.content.elements, {collection: 'productCategory'}),
                    (state) => ({
                      ...state,
                      pageSize: productCategorySuccessResponse.data.content.pageSize,
                      totalElements: productCategorySuccessResponse.data.content.totalElements
                    }))
                },
                error: (error) => {
                  patchState(store, state => ({...state, error}));
                },
                finalize: () => patchState(store, (state) => ({...state, loading: false}))
              })
            )
        )
      )
    ),
    findProductCategoryById(id: string): WritableSignal<ProductCategoryModel | undefined> {
      return signal(store.productCategoryEntities().find((productCategory: ProductCategoryModel) => productCategory.id === id));
    }
  }))
);
