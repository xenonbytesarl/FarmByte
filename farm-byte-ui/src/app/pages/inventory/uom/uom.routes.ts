import {Routes} from "@angular/router";
import {uomsResolver} from "./resolvers/uom.resolver";
import {uomCategoriesResolver} from "../uom-category/resolvers/uom-category.resolver";

export const uomRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/uom-tree/uom-tree.component').then(m => m.UomTreeComponent),
    resolve: {
      uoms: uomsResolver,
      uomCategories: uomCategoriesResolver
    }
  }
]
