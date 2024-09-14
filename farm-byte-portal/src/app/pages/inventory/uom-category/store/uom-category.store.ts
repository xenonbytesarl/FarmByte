import {patchState, signalStore, type, withMethods, withState} from "@ngrx/signals";
import {addEntity, withEntities} from "@ngrx/signals/entities";
import {UomCategoryModel} from "../model/uom-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {Direction} from "../../../../core/enums/direction.enum";
import {inject} from "@angular/core";
import {UomCategoryService} from "../service/uom-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";

type UomCategory = UomCategoryModel;

type UomCategoryPage = {
  uomCategoryPage: PageModel<UomCategoryModel> | undefined;
  loading: boolean;
  error: any;
};

const uomCategoryPageInitialState: UomCategoryPage = {
  uomCategoryPage: undefined,
  loading: false,
  error: undefined
};


export const UomCategoryStore = signalStore(
  withState(uomCategoryPageInitialState),
  withEntities({entity: type<UomCategory>(), collection: 'uomCategory'}),
  withMethods((store, uomCategoryService = inject(UomCategoryService)) => ({
    findUomCategories: rxMethod<FindParamModel>(
      pipe(
        tap(() => patchState(store, (state) => ({...state, loading: true}))),
        switchMap((findParamModel: FindParamModel) =>
          uomCategoryService.findUomCategories(findParamModel)
            .pipe(
              tapResponse({
                next: (uomCategoryPage: PageModel<UomCategoryModel>) => {
                  patchState(store, (state) => ({
                    ...state,
                    loading: false,
                    uomCategoryPage
                  }))
                },
                error: (error) => {
                  patchState(store, state => ({...state, error, loading: false}));
                }
              })
            )
        )
      )
    )
  })),
);
