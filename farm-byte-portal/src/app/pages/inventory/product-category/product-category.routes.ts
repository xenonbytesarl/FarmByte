import {Routes} from "@angular/router";
import {productCategoriesResolver} from "./resolvers/product-category.resolver";

export const productCategoryRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/tree/product-category-tree.component').then(m => m.ProductCategoryTreeComponent),
    resolve: {
      productCategories: productCategoriesResolver
    }
  }
]
