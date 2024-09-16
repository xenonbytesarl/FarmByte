import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {UomModel} from "../model/uom.model";
import {patchState, signalStore, withComputed, withMethods, withState} from "@ngrx/signals";
import {computed, inject} from "@angular/core";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {debounceTime, distinctUntilChanged, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {UomService} from "../services/uom.service";
import {withDevtools} from "@angular-architects/ngrx-toolkit";
import {SearchParamModel} from "../../../../core/model/search-param.model";

type UomState = {
  uomPage: SuccessResponseModel<PageModel<UomModel>> ;
  loading: boolean;
  error: any;
};

const uomInitialState: UomState= {
  uomPage: {
    data: {
      content: {
        elements: []
      }
    }
  },
  loading: false,
  error: null
};

export const UomStore = signalStore(
  {providedIn: 'root'},
  withState(uomInitialState),
  withDevtools('uom'),
  withMethods((store, uomService = inject(UomService)) => ({
    findUoms: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          uomService.findUoms$(findParamModel)
            .pipe(
              tapResponse({
                next: (uomPage: SuccessResponseModel<PageModel<UomModel>>) => {
                  patchState(store, (state) => ({
                    ...state,
                    uomPage
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
    searchUoms: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(300),
        distinctUntilChanged(),
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((searchParamModel: SearchParamModel) =>
          uomService.searchUoms$(searchParamModel)
            .pipe(
              tapResponse({
                next: (uomPage: SuccessResponseModel<PageModel<UomModel>>) => {
                  patchState(store, (state) => ({
                    ...state,
                    uomPage
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
    )
  })),
  withComputed((store) => {
    return {
      uomDataSource: computed(() =>
        store.uomPage().data.content?.elements
      ),
      uomTotalElements: computed(() =>
        store.uomPage().data.content?.totalElements
      ),
      uomTotalPageSize: computed(() =>
        store.uomPage().data.content?.pageSize
      )
    }
  })
);
