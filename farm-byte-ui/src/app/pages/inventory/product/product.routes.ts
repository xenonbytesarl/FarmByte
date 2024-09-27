import {Routes} from "@angular/router";
import {findUomsResolver} from "../uom/resolvers/uom.resolver";
import {productsResolver} from "./resolver/product.resolver";
import {productCategoriesResolver} from "../product-category/resolvers/product-category.resolver";

export const productRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/product-tree/product-tree.component').then(m => m.ProductTreeComponent),
    resolve: {
      products: productsResolver,
      productCategories: productCategoriesResolver,
      uoms: findUomsResolver
    }
  }
]
