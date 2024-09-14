import {patchState, signalStore, type, withComputed, withMethods, withState} from "@ngrx/signals";
import {addEntity, withEntities} from "@ngrx/signals/entities";
import {UomCategoryModel} from "../model/uom-category.model";
import {PageModel} from "../../../../core/model/page.model";
import {Direction} from "../../../../core/enums/direction.enum";
import {computed, inject} from "@angular/core";
import {UomCategoryService} from "../service/uom-category.service";
import {rxMethod} from "@ngrx/signals/rxjs-interop";
import {pipe, switchMap, tap} from "rxjs";
import {tapResponse} from "@ngrx/operators";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";

type UomCategory = UomCategoryModel;

type UomCategoryPage = {
  uomCategoryPage: SuccessResponseModel<PageModel<UomCategoryModel>> ;
  loading: boolean;
  error: any;
};

const uomCategoryPageInitialState: UomCategoryPage = {
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
  withState(uomCategoryPageInitialState),
  withEntities({entity: type<UomCategory>(), collection: 'uomCategory'}),
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
