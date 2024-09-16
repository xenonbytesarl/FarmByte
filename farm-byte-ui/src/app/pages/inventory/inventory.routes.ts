import {Routes} from "@angular/router";

export const inventoryRoutes: Routes = [
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: 'products',
    loadChildren: () => import('./product/product.routes').then(m => m.productRoutes)
  },
  {
    path: 'product-categories',
    loadChildren: () => import('./product-category/product-category.routes').then(m => m.productCategoryRoutes)
  },
  {
    path: 'uom-categories',
    loadChildren: () => import('./uom-category/uom-category.routes').then(m => m.uomCategoryRoutes)
  },
  {
    path: 'uoms',
    loadChildren: () => import('./uom/uom.routes').then(m => m.uomRoutes)
  }
]
