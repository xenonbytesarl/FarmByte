import {Routes} from "@angular/router";

export const productRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./tree/product-tree.component').then(m => m.ProductTreeComponent)
  }
]
