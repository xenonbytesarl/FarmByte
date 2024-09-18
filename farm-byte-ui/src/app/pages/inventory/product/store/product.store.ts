import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {ProductModel} from "../model/product.model";
import {patchState, signalStore, type, withMethods, withState} from "@ngrx/signals";
import {inject, signal, WritableSignal} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {debounceTime, distinctUntilChanged, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {ProductService} from "../services/product.service";
import {withDevtools} from "@angular-architects/ngrx-toolkit";
import {SearchParamModel} from "../../../../core/model/search-param.model";
import {ProductCategoryModel} from "../../product-category/model/product-category.model";
import {ProductCategoryStore} from "../../product-category/store/product-category.store";
import {addEntities, addEntity, withEntities} from "@ngrx/signals/entities";
import {UomStore} from "../../uom/store/uom.store";
import {UomModel} from "../../uom/model/uom.model";

type ProductState = {
  pageSize: number;
  totalElements: number;
  loading: boolean;
  error: any;
};

const productInitialState: ProductState= {
  pageSize: 0,
  totalElements: 0,
  loading: false,
  error: null
};

export const ProductStore = signalStore(
  {providedIn: 'root'},
  withState(productInitialState),
  withEntities({ entity: type<ProductModel>(), collection: 'product' }),
  withDevtools('productState'),
  withMethods((store,
               productService = inject(ProductService),
               productCategoryStore =inject(ProductCategoryStore),
               uomStore = inject(UomStore))   => ({
    findProducts: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          productService.findProducts$(findParamModel)
            .pipe(
              tapResponse({
                next: (productSuccessResponse: SuccessResponseModel<PageModel<ProductModel>>) => {
                  patchState(
                    store,
                    addEntities(productSuccessResponse.data.content.elements, {collection: 'product'}),
                    (state) => ({
                      ...state,
                      pageSize: productSuccessResponse.data.content.pageSize,
                      totalElements: productSuccessResponse.data.content.totalElements
                    }));
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
    searchProducts: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((searchParamModel: SearchParamModel) =>
          productService.searchProducts$(searchParamModel)
            .pipe(
              tapResponse({
                next: (productSuccessResponse: SuccessResponseModel<PageModel<ProductModel>>) => {
                  patchState(
                    store,
                    addEntities(productSuccessResponse.data.content.elements, {collection: 'product'}),
                    (state) => ({
                      ...state,
                      pageSize: productSuccessResponse.data.content.pageSize,
                      totalElements: productSuccessResponse.data.content.totalElements
                    }));
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
    findProductById: rxMethod<string>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((id: string) =>
          productService.findProductById$(id)
            .pipe(
              tapResponse({
                next: (product: SuccessResponseModel<ProductModel>) => {
                  patchState(store, addEntity(product.data.content, {collection: 'product'}));
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
    findProductCategoryById(productCategoryId: string): WritableSignal<ProductCategoryModel | undefined> {
      return signal(productCategoryStore.productCategoryEntities().find((productCategory: ProductCategoryModel) => productCategory.id === productCategoryId));
    },
    findUomById(uomId: string): WritableSignal<UomModel | undefined> {
      return signal(uomStore.uomEntities().find((uom: UomModel) => uom.id === uomId));
    }
  }))
);
