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
import {addEntity, setAllEntities, setEntity, withEntities} from "@ngrx/signals/entities";
import {DEBOUNCE_TIMEOUT} from "../../../../core/constants/app.constant";
import {setError, setLoaded, setLoading, withRequestStatus} from "../../../../core/store/request.store";
import {setPageSize, setTotalElements, withPageStatus} from "../../../../core/store/page.store";
import {MessageService} from "primeng/api";

type UomState = {
  selectedUom: UomModel | undefined
};

const uomInitialState: UomState= {
  selectedUom: undefined
};

export const UomStore = signalStore(
  {providedIn: 'root'},
  withState(uomInitialState),
  withRequestStatus(),
  withPageStatus(),
  withEntities({ entity: type<UomModel>(), collection: 'uom' }),
  withDevtools('uomState'),
  withMethods((store,
               uomService = inject(UomService),
               uomCategoryStore = inject(UomCategoryStore),
               messageService = inject(MessageService))   => ({
    findUoms: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((findParamModel: FindParamModel) =>
          uomService.findUoms$(findParamModel)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<PageModel<UomModel>>) => {
                  patchState(
                    store,
                    setAllEntities(uomSuccessResponse.data.content.elements, {collection: 'uom'}),
                    setPageSize(uomSuccessResponse.data.content.pageSize as number),
                    setTotalElements(uomSuccessResponse.data.content.totalElements as number)
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
    searchUoms: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(DEBOUNCE_TIMEOUT),
        distinctUntilChanged(),
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((searchParamModel: SearchParamModel) =>
          uomService.searchUoms$(searchParamModel)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<PageModel<UomModel>>) => {
                  patchState(
                    store,
                    setAllEntities(uomSuccessResponse.data.content.elements, {collection: 'uom'}),
                    setPageSize(uomSuccessResponse.data.content.pageSize as number),
                    setTotalElements(uomSuccessResponse.data.content.totalElements as number)
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
    findUomById: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((id: string) =>
          uomService.findUomById$(id)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<UomModel>) => {
                 patchState(store, (state)=>({
                   ...state,
                   selectedUom: uomSuccessResponse.data.content
                 }));
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
    createUom: rxMethod<UomModel>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((uom: UomModel) =>
          uomService.createUom$(uom)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<UomModel>) => {
                  patchState(
                    store,
                    addEntity(uomSuccessResponse.data.content, {collection: 'uom'}),
                    setTotalElements(store.totalElements() + 1 as number)
                  );
                  messageService.add({severity: 'success', summary: 'Info', detail: uomSuccessResponse.message});
                },
                error: (error) => {
                  patchState(store, setError(error));
                  messageService.add({severity: 'error', summary: 'Info', detail: error as string});
                },
                finalize: () => patchState(store, setLoaded())
              })
            )
        )
      )
    ),
    updateUom: rxMethod<UomModel>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((uom: UomModel) =>
          uomService.updateUom$(uom)
            .pipe(
              tapResponse({
                next: (uomSuccessResponse: SuccessResponseModel<UomModel>) => {
                  patchState(
                    store,
                    setEntity(uomSuccessResponse.data.content, {collection: 'uom'}),
                    (state => ({
                      selectedUom: uomSuccessResponse.data.content
                    }))
                  );
                  messageService.add({severity: 'info', summary: 'Info', detail: uomSuccessResponse.message});
                },
                error: (error) => {
                  patchState(store, setError(error));
                  messageService.add({severity: 'error', summary: 'Info', detail: error as string});
                },
                finalize: () => patchState(store, setLoaded())
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
