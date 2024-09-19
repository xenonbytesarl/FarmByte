import {patchState, signalStore, type, withMethods, withState} from "@ngrx/signals";
import {UomCategoryModel} from "../model/uom-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {inject, signal, WritableSignal} from "@angular/core";
import {UomCategoryService} from "../service/uom-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {debounceTime, distinctUntilChanged, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {withDevtools} from "@angular-architects/ngrx-toolkit";
import {SearchParamModel} from "../../../../core/model/search-param.model";
import {addEntities, setAllEntities, withEntities} from "@ngrx/signals/entities";
import {DEBOUNCE_TIMEOUT} from "../../../../core/constants/app.constant";

type UomCategoryState = {
  pageSize: number;
  totalElements: number;
  loading: boolean;
  error: any;
};

const uomCategoryInitialState: UomCategoryState = {
  pageSize: 0,
  totalElements: 0,
  loading: false,
  error: null
};


export const UomCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(uomCategoryInitialState),
  withEntities({ entity: type<UomCategoryModel>(), collection: 'uomCategory' }),
  withDevtools('uomCategoryState'),
  withMethods((store, uomCategoryService = inject(UomCategoryService)) => ({
    findUomCategories: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          uomCategoryService.findUomCategories$(findParamModel)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<PageModel<UomCategoryModel>>) => {
                  patchState(
                    store,
                    addEntities(uomCategorySuccessResponse.data.content.elements, {collection: 'uomCategory'}),
                    (state) => ({
                      ...state,
                      pageSize: uomCategorySuccessResponse.data.content.pageSize,
                      totalElements: uomCategorySuccessResponse.data.content.totalElements
                    })
                  );
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
    searchUomCategories: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(DEBOUNCE_TIMEOUT),
        distinctUntilChanged(),
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((searchParamModel: SearchParamModel) =>
          uomCategoryService.searchUomCategories$(searchParamModel)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<PageModel<UomCategoryModel>>) => {
                  patchState(
                    store,
                    setAllEntities(uomCategorySuccessResponse.data.content.elements, {collection: 'uomCategory'}),
                    (state) => ({
                      ...state,
                      pageSize: uomCategorySuccessResponse.data.content.pageSize,
                      totalElements: uomCategorySuccessResponse.data.content.totalElements
                    })
                  );
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
      return signal(store.uomCategoryEntities().find((uomCategory: UomCategoryModel) => uomCategory.id === id));
    }
  }))
);
