import {Routes} from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'inventory',
    pathMatch: 'full',
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./pages/dashboard/dashboard.routes').then(m => m.dashboardRoutes)
  },
  {
    path: 'inventory',
    loadChildren: () => import('./pages/inventory/inventory.routes').then(m => m.inventoryRoutes)
  }
];
