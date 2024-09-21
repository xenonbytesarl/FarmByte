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
import {Router} from "@angular/router";

type UomCategoryState = {
  pageSize: number;
  totalElements: number;
  loading: boolean;
  error: any;
  formMode: FormMode | undefined,
  selectedUomCategory: UomCategoryModel | undefined,
};

const uomCategoryInitialState: UomCategoryState = {
  pageSize: 0,
  totalElements: 0,
  loading: false,
  error: null,
  formMode: undefined,
  selectedUomCategory: undefined,
};


export const UomCategoryStore = signalStore(
  {providedIn: 'root'},
  withState(uomCategoryInitialState),
  withEntities({ entity: type<UomCategoryModel>(), collection: 'uomCategory' }),
  withDevtools('uomCategoryState'),
  withMethods((store,
               uomCategoryService = inject(UomCategoryService),
               messageService = inject(MessageService),
               router = inject(Router)) => ({
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
    findUomCategoryById: rxMethod<string>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
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
                  patchState(store, state => ({...state, error}));
                },
                finalize: () => patchState(store, (state) => ({...state, loading: false}))
              })
            )
          )
        )
      ),
    createUomCategory: rxMethod<UomCategoryModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((uomCategory: UomCategoryModel) =>
          uomCategoryService.createUomCategory$(uomCategory)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<UomCategoryModel>) => {
                  patchState(
                    store,
                    addEntity(uomCategorySuccessResponse.data.content, {collection: 'uomCategory'}),
                    (state) => ({
                      ...state,
                      totalElements: state.totalElements + 1
                    })
                  );
                  messageService.add({severity: 'success', summary: 'Info', detail: uomCategorySuccessResponse.message});
                  router.navigate([`/inventory/uom-categories/details/${uomCategorySuccessResponse.data.content.id}`]);
                },
                error: (error) => {
                  patchState(store, state => ({...state, error}));
                  messageService.add({severity: 'error', summary: 'Info', detail: error as string});
                },
                finalize: () => patchState(store, (state) => ({...state, loading: false}))
              })
            )
        )
      )
    ),
    updateUomCategory: rxMethod<UomCategoryModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((uomCategory: UomCategoryModel) =>
          uomCategoryService.updateUomCategory$(uomCategory)
            .pipe(
              tapResponse({
                next: (uomCategorySuccessResponse: SuccessResponseModel<UomCategoryModel>) => {
                  patchState(
                    store,
                    setEntity(uomCategorySuccessResponse.data.content, {collection: 'uomCategory'}),
                    (state => ({
                      formMode: FormMode.READ,
                      selectedUomCategory: uomCategorySuccessResponse.data.content
                    }))
                  );
                  messageService.add({severity: 'info', summary: 'Info', detail: uomCategorySuccessResponse.message});
                },
                error: (error) => {
                  patchState(store, state => ({...state, error}));
                  messageService.add({severity: 'error', summary: 'Info', detail: error as string});
                },
                finalize: () => patchState(store, (state) => ({...state, loading: false}))
              })
            )
        )
      )
    ),
    initForm(mode: FormMode): void {
      patchState(store, state => ({...state, formMode: mode}))
    },
    clearSelectedUomCategory(): void {
      patchState(store, state => ({...state, selectedUomCategory: undefined}))
    }
  }))
);
