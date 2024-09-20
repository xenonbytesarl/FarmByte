import {Routes} from "@angular/router";
import {productCategoriesResolver} from "./resolvers/product-category.resolver";

export const productCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/product-category-tree/product-category-tree.component').then(m => m.ProductCategoryTreeComponent),
    resolve: {
      productCategories: productCategoriesResolver
    }
  },
  {
    path: 'new',
    loadComponent: () => import('./screen/product-category-form/product-category-form.component').then(m => m.ProductCategoryFormComponent),
    resolve: {
      productCategories: productCategoriesResolver
    }
  }
]
