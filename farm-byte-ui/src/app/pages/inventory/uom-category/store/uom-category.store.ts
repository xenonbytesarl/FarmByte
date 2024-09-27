import {patchState, signalStore, type, withMethods, withState} from "@ngrx/signals";
import {UomCategoryModel} from "../model/uom-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {inject} from "@angular/core";
import {UomCategoryService} from "../service/uom-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {debounceTime, distinctUntilChanged, pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {withDevtools} from "@angular-architects/ngrx-toolkit";
import {SearchParamModel} from "../../../../core/model/search-param.model";
import {addEntity, setAllEntities, setEntity, withEntities} from "@ngrx/signals/entities";
import {DEBOUNCE_TIMEOUT} from "../../../../core/constants/app.constant";
import {MessageService} from "primeng/api";
import {FormMode} from "../../../../core/enums/form-mode.enum";
import {setError, setLoaded, setLoading, withRequestStatus} from "../../../../core/store/request.store";
import {setPageSize, setTotalElements, withPageStatus} from "../../../../core/store/page.store";
import {setFormMode, withFormModeStatus} from "../../../../core/store/form-mode.store";

type UomCategoryState = {
  selectedUomCategory: UomCategoryModel | undefined,
};

const uomCategoryInitialState: UomCategoryState = {
  selectedUomCategory: undefined
};


export const UomCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(uomCategoryInitialState),
  withRequestStatus(),
  withPageStatus(),
  withFormModeStatus(),
  withEntities({ entity: type<UomCategoryModel>(), collection: 'uomCategory' }),
  withDevtools('uomCategoryState'),
  withMethods((store,
               uomCategoryService = inject(UomCategoryService),
               messageService = inject(MessageService)) => ({
    findUomCategories: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((findParamModel: FindParamModel) =>
          uomCategoryService.findUomCategories$(findParamModel)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<PageModel<UomCategoryModel>>) => {
                  patchState(
                    store,
                    setAllEntities(uomCategorySuccessResponse.data.content.elements, {collection: 'uomCategory'}),
                    setPageSize(uomCategorySuccessResponse.data.content.pageSize as number),
                    setTotalElements(uomCategorySuccessResponse.data.content.totalElements as number)
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
    searchUomCategories: rxMethod<SearchParamModel>(
      pipe(
        debounceTime(DEBOUNCE_TIMEOUT),
        distinctUntilChanged(),
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((searchParamModel: SearchParamModel) =>
          uomCategoryService.searchUomCategories$(searchParamModel)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<PageModel<UomCategoryModel>>) => {
                  patchState(
                    store,
                    setAllEntities(uomCategorySuccessResponse.data.content.elements, {collection: 'uomCategory'}),
                    setPageSize(uomCategorySuccessResponse.data.content.pageSize as number),
                    setTotalElements(uomCategorySuccessResponse.data.content.totalElements as number)
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
    findUomCategoryById: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((uomCategoryId: string) =>
          uomCategoryService.findUomCategoryById$(uomCategoryId)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<UomCategoryModel>) => {
                  patchState(
                    store,
                    (state) => ({
                      ...state,
                      selectedUomCategory: uomCategorySuccessResponse.data.content
                    })
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
    createUomCategory: rxMethod<UomCategoryModel>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((uomCategory: UomCategoryModel) =>
          uomCategoryService.createUomCategory$(uomCategory)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<UomCategoryModel>) => {
                  patchState(
                    store,
                    addEntity(uomCategorySuccessResponse.data.content, {collection: 'uomCategory'}),
                    setTotalElements(store.totalElements() + 1 as number)
                  );
                  messageService.add({severity: 'success', summary: 'Info', detail: uomCategorySuccessResponse.message});
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
    updateUomCategory: rxMethod<UomCategoryModel>(
      pipe(
        tap(() => patchState(store, setLoading(), setError(undefined))),
        switchMap((uomCategory: UomCategoryModel) =>
          uomCategoryService.updateUomCategory$(uomCategory)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<UomCategoryModel>) => {
                  patchState(
                    store,
                    setEntity(uomCategorySuccessResponse.data.content, {collection: 'uomCategory'}),
                    setFormMode(FormMode.READ),
                    (state => ({
                      selectedUomCategory: uomCategorySuccessResponse.data.content
                    }))
                  );
                  messageService.add({severity: 'info', summary: 'Info', detail: uomCategorySuccessResponse.message});
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
    initForm(mode: FormMode): void {
      patchState(store, setFormMode(mode))
    },
    clearSelectedUomCategory(): void {
      patchState(store, state => ({...state, selectedUomCategory: undefined}))
    }
  }))
);
