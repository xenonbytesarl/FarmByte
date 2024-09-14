import {Routes} from "@angular/router";

export const stockLocationRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./tree/stock-location-tree.component').then(m => m.StockLocationTreeComponent)
  }
]
