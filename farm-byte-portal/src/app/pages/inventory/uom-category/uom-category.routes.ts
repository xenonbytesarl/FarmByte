import {Routes} from "@angular/router";
import {uomCategoriesResolver} from "./resolvers/uom-category.resolver";

export const uomCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./tree/uom-category-tree.component').then(m => m.UomCategoryTreeComponent),
    resolve: {
      uomCategories: uomCategoriesResolver
    }
  }
]
