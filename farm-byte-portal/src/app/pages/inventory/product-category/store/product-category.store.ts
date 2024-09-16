import {patchState, signalStore, withComputed, withMethods, withState} from "@ngrx/signals";
import {ProductCategoryModel} from "../model/product-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {computed, inject, signal, WritableSignal} from "@angular/core";
import {ProductCategoryService} from "../service/product-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {withDevtools} from "@angular-architects/ngrx-toolkit";

type ProductCategoryState = {
  productCategoryPage: SuccessResponseModel<PageModel<ProductCategoryModel>> ;
  loading: boolean;
  error: any;
};

const productCategoryInitialState: ProductCategoryState = {
  productCategoryPage: {
    data: {
      content: {
        elements: []
      }
    }
  },
  loading: false,
  error: null
};


export const ProductCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(productCategoryInitialState),
  withDevtools('product-category'),
  withMethods((store, productCategoryService = inject(ProductCategoryService)) => ({
    findProductCategories: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          productCategoryService.findProductCategories$(findParamModel)
            .pipe(
              tapResponse({
                next: (productCategoryPage: SuccessResponseModel<PageModel<ProductCategoryModel>>) => {
                  patchState(store, (state) => ({
                    ...state,
                    productCategoryPage
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
      return signal((store.productCategoryPage().data.content?.elements as ProductCategoryModel[]).find((productCategory: ProductCategoryModel) => productCategory.id === id));
    }



  })),
  withComputed((store) => {
    return {
      productCategoryDataSource: computed(() =>
        store.productCategoryPage().data.content?.elements
      ),
      productCategoryTotalElements: computed(() =>
        store.productCategoryPage().data.content?.totalElements
      ),
      productCategoryTotalPageSize: computed(() =>
        store.productCategoryPage().data.content?.pageSize
      )
    }
  })
);
