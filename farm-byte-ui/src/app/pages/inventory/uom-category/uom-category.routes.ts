import {Routes} from "@angular/router";
import {uomCategoriesResolver} from "./resolvers/uom-category.resolver";

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
    resolve: {
      uomCategories: uomCategoriesResolver
    }
  }
]
