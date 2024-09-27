import {Routes} from "@angular/router";
import {
  findUomCategoriesResolver, findUomCategoryByIdResolver,
} from "./resolvers/uom-category.resolver";

export const uomCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/uom-category-tree/uom-category-tree.component').then(m => m.UomCategoryTreeComponent),
    resolve: {
      uomCategories: findUomCategoriesResolver
    }
  },
  {
    path: 'new',
    loadComponent:  () => import('./screen/uom-category-form-new/uom-category-form-new.component').then(m => m.UomCategoryFormNewComponent)
  },
  {
    path: 'edit/:uomCategoryId',
    loadComponent:  () => import('./screen/uom-category-form-edit/uom-category-form-edit.component').then(m => m.UomCategoryFormEditComponent),
    resolve: {
      uomCategory: findUomCategoryByIdResolver
    }
  }
]
