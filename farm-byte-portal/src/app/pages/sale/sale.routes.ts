import {Routes} from "@angular/router";

export const saleRoutes: Routes = [
  {
    path: '',
    redirectTo: 'sale-quotations',
    pathMatch: 'full',
  },
  {
    path: 'sale-quotations',
    loadComponent: () => import('./sale-quotation/sale-quotation.component').then(m => m.SaleQuotationComponent),
  }
]
