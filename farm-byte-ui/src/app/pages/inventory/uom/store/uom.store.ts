import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {UomModel} from "../model/uom.model";
import {patchState, signalStore, type, withMethods, withState} from "@ngrx/signals";
import {inject, signal, WritableSignal} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {debounceTime, distinctUntilChanged, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {UomService} from "../services/uom.service";
import {withDevtools} from "@angular-architects/ngrx-toolkit";
import {SearchParamModel} from "../../../../core/model/search-param.model";
import {UomCategoryModel} from "../../uom-category/model/uom-category.model";
import {UomCategoryStore} from "../../uom-category/store/uom-category.store";
import {addEntities, addEntity, setAllEntities, withEntities} from "@ngrx/signals/entities";
import {DEBOUNCE_TIMEOUT} from "../../../../core/constants/app.constant";

type UomState = {
  pageSize: number;
  totalElements: number;
  loading: boolean;
  error: any;
};

const uomInitialState: UomState= {
  pageSize: 0,
  totalElements: 0,
  loading: false,
  error: null
};

export const UomStore = signalStore(
  {providedIn: 'root'},
  withState(uomInitialState),
  withEntities({ entity: type<UomModel>(), collection: 'uom' }),
  withDevtools('uomState'),
  withMethods((store, uomService = inject(UomService), uomCategoryStore =inject(UomCategoryStore))   => ({
    findUoms: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          uomService.findUoms$(findParamModel)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<PageModel<UomModel>>) => {
                  patchState(
                    store,
                    addEntities(uomSuccessResponse.data.content.elements, {collection: 'uom'}),
                    (state) => ({
                    ...state,
                    pageSize: uomSuccessResponse.data.content.pageSize,
                    totalElements: uomSuccessResponse.data.content.totalElements
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
    searchUoms: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(DEBOUNCE_TIMEOUT),
        distinctUntilChanged(),
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((searchParamModel: SearchParamModel) =>
          uomService.searchUoms$(searchParamModel)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<PageModel<UomModel>>) => {
                  patchState(
                    store,
                    setAllEntities(uomSuccessResponse.data.content.elements, {collection: 'uom'}),
                    (state) => ({
                      ...state,
                      pageSize: uomSuccessResponse.data.content.pageSize,
                      totalElements: uomSuccessResponse.data.content.totalElements
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
    findUomById: rxMethod<string>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((id: string) =>
          uomService.findUomById$(id)
            .pipe(
              tapResponse({
                next: (uom: SuccessResponseModel<UomModel>) => {
                 patchState(store, addEntity(uom.data.content, {collection: 'uom'}));
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
    findUomCategoryById(uomCategoryId: string): WritableSignal<UomCategoryModel | undefined> {
      return signal(uomCategoryStore.uomCategoryEntities().find((uomCategory: UomCategoryModel) => uomCategory.id === uomCategoryId));
    }
  }))
);
