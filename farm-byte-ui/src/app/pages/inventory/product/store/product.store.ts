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
import {addEntity, setAllEntities, withEntities} from "@ngrx/signals/entities";
import {UomStore} from "../../uom/store/uom.store";
import {UomModel} from "../../uom/model/uom.model";
import {DEBOUNCE_TIMEOUT} from "../../../../core/constants/app.constant";
import {setError, setLoaded, setLoading, withRequestStatus} from "../../../../core/store/request.store";
import {setPageSize, setTotalElements, withPageStatus} from "../../../../core/store/page.store";
import {withFormModeStatus} from "../../../../core/store/form-mode.store";

type ProductState = {
  selectedProduct: ProductModel | undefined
};

const productInitialState: ProductState= {
  selectedProduct: undefined
};

export const ProductStore = signalStore(
  {providedIn: 'root'},
  withState(productInitialState),
  withRequestStatus(),
  withPageStatus(),
  withFormModeStatus(),
  withEntities({ entity: type<ProductModel>(), collection: 'product' }),
  withDevtools('productState'),
  withMethods((store,
               productService = inject(ProductService),
               productCategoryStore =inject(ProductCategoryStore),
               uomStore = inject(UomStore))   => ({
    findProducts: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((findParamModel: FindParamModel) =>
          productService.findProducts$(findParamModel)
            .pipe(
              tapResponse({
                next: (productSuccessResponse: SuccessResponseModel<PageModel<ProductModel>>) => {
                  patchState(
                    store,
                    setAllEntities(productSuccessResponse.data.content.elements, {collection: 'product'}),
                    setPageSize(productSuccessResponse.data.content.pageSize as number),
                    setTotalElements(productSuccessResponse.data.content.totalElements as number)
                  );
                },
                error: (error) => {
                  patchState(store, setError(error));
                },
                finalize: () => patchState(store, setLoaded())
              })
            )
        )
      )
    ),
    searchProducts: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(DEBOUNCE_TIMEOUT),
        distinctUntilChanged(),
        tap(() => patchState(store, setLoading())),
        switchMap((searchParamModel: SearchParamModel) =>
          productService.searchProducts$(searchParamModel)
            .pipe(
              tapResponse({
                next: (productSuccessResponse: SuccessResponseModel<PageModel<ProductModel>>) => {
                  patchState(
                    store,
                    setAllEntities(productSuccessResponse.data.content.elements, {collection: 'product'}),
                    setPageSize(productSuccessResponse.data.content.pageSize as number),
                    setTotalElements(productSuccessResponse.data.content.totalElements as number)
                  );
                },
                error: (error) => {
                  patchState(store, setError(error));
                },
                finalize: () => patchState(store, setLoaded())
              })
            )
        )
      )
    ),
    findProductById: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((id: string) =>
          productService.findProductById$(id)
            .pipe(
              tapResponse({
                next: (product: SuccessResponseModel<ProductModel>) => {
                  patchState(store, addEntity(product.data.content, {collection: 'product'}));
                },
                error: (error) => {
                  patchState(store, setError(error));
                },
                finalize: () => patchState(store, setLoaded())
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
