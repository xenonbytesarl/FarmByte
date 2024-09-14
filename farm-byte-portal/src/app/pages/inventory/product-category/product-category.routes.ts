import {Routes} from "@angular/router";

export const productCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./tree/product-category-tree.component').then(m => m.ProductCategoryTreeComponent)
  }
]
