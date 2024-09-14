import {Routes} from "@angular/router";

export const uomCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./tree/uom-category-tree.component').then(m => m.UomCategoryTreeComponent)
  }
]
