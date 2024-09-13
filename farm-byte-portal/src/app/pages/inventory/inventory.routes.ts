import {Routes} from "@angular/router";

export const inventoryRoutes: Routes = [
  {
    path: '',
    redirectTo: 'products',
    pathMatch: 'full',
  },
  {
    path: 'products',
    loadChildren: () => import('./products/product.routes').then(m => m.productRoutes)
  },
  {
    path: 'product-categories',
    loadChildren: () => import('./product-categories/product-category.routes').then(m => m.productCategoryRoutes)
  },
  {
    path: 'category-unit-of-measures',
    loadChildren: () => import('./category-unit-of-measures/category-unit-of-measure.routes').then(m => m.categoryUnitOfMeasureRoutes)
  },
  {
    path: 'unit-of-measures',
    loadChildren: () => import('./unit-of-measures/unit-of-measure.routes').then(m => m.unitOfMeasureRoutes)
  },
  {
    path: 'locations',
    loadChildren: () => import('./locations/location.routes').then(m => m.locationRoutes)
  }
]
