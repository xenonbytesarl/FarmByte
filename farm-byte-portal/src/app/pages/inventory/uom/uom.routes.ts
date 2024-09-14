import {Routes} from "@angular/router";

export const uomRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./tree/uom-tree.component').then(m => m.UomTreeComponent)
  }
]
