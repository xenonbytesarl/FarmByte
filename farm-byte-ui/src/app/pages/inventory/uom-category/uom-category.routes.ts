import {Routes} from "@angular/router";
import {
  uomCategoriesResolver,
  uomCategoryDetailResolver,
  uomCategoryInitFormResolver, uomCategoryResetSelectUomCategoryResolver
} from "./resolvers/uom-category.resolver";
import {FormMode} from "../../../core/enums/form-mode.enum";

export const uomCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/uom-category-tree/uom-category-tree.component').then(m => m.UomCategoryTreeComponent),
    resolve: {
      uomCategories: uomCategoriesResolver
    }
  },
  {
    path: 'new',
    loadComponent:  () => import('./screen/uom-category-form/uom-category-form.component').then(m => m.UomCategoryFormComponent),
    data: {
      mode: FormMode.WRITE
    },
    resolve: {
      uomCategoryInitForm: uomCategoryInitFormResolver,
      uomCategoryResetSelectUomCategory: uomCategoryResetSelectUomCategoryResolver
    }
  },
  {
    path: 'details/:uomCategoryId',
    loadComponent:  () => import('./screen/uom-category-form/uom-category-form.component').then(m => m.UomCategoryFormComponent),
    data: {
      mode: FormMode.READ
    },
    resolve: {
      uomCategoryId: uomCategoryDetailResolver,
      uomCategoryInitForm: uomCategoryInitFormResolver
    }
  }
]
