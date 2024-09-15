import {patchState, signalStore, withComputed, withMethods, withState} from "@ngrx/signals";
import {UomCategoryModel} from "../model/uom-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {computed, inject, Signal, signal, WritableSignal} from "@angular/core";
import {UomCategoryService} from "../service/uom-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {map, of, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";

type UomCategoryState = {
  uomCategoryPage: SuccessResponseModel<PageModel<UomCategoryModel>> ;
  loading: boolean;
  error: any;
};

const uomCategoryInitialState: UomCategoryState = {
  uomCategoryPage: {
    data: {
      content: {
        elements: []
      }
    }
  },
  loading: false,
  error: null
};


export const UomCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(uomCategoryInitialState),
  withMethods((store, uomCategoryService = inject(UomCategoryService)) => ({
    findUomCategories: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          uomCategoryService.findUomCategories$(findParamModel)
            .pipe(
              tapResponse({
                next: (uomCategoryPage: SuccessResponseModel<PageModel<UomCategoryModel>>) => {
                  patchState(store, (state) => ({
                    ...state,
                    uomCategoryPage
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
    findUomCategoryById(id: string): WritableSignal<UomCategoryModel | undefined> {
      return signal((store.uomCategoryPage().data.content?.elements as UomCategoryModel[]).find((uomCategory: UomCategoryModel) => uomCategory.id === id));
    }



  })),
  withComputed((store) => {
    return {
      uomCategoryDataSource: computed(() =>
        store.uomCategoryPage().data.content?.elements
      ),
      uomCategoryTotalElements: computed(() =>
        store.uomCategoryPage().data.content?.totalElements
      ),
      uomCategoryTotalPageSize: computed(() =>
        store.uomCategoryPage().data.content?.pageSize
      )
    }
  })
);
