import {Routes} from "@angular/router";
import {uomFindByIdResolver, uomsResolver} from "./resolvers/uom.resolver";
import {uomCategoriesResolver} from "../uom-category/resolvers/uom-category.resolver";

export const uomRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./screen/uom-tree/uom-tree.component').then(m => m.UomTreeComponent),
    resolve: {
      uoms: uomsResolver,
      uomCategories: uomCategoriesResolver
    }
  },
  {
    path: 'new',
    loadComponent: () => import('./screen/uom-form-new/uom-form-new.component').then(m => m.UomFormNewComponent),
    resolve: {
      uomCategories: uomCategoriesResolver
    }
  },
  {
    path: 'edit/:uomId',
    loadComponent: () => import('./screen/uom-form-edit/uom-form-edit.component').then(m => m.UomFormEditComponent),
    resolve: {
      uomCategories: uomCategoriesResolver,
      uomFindById: uomFindByIdResolver
    }
  }
]
