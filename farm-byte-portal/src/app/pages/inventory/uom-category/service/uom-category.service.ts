import {inject, Injectable} from '@angular/core';
import {catchError, Observable} from "rxjs";
import {GlobalService} from "../../../../core/services/global.service";
import {Direction} from "../../../../core/enums/direction.enum";
import {PageModel} from "../../../../core/model/page.model";
import {UomCategoryModel} from "../model/uom-category.model";
import {FindParamModel} from "../../../../core/model/find-param.model";

@Injectable({
  providedIn: 'root'
})
export class UomCategoryService extends GlobalService{

  findUomCategories (findParamModel: FindParamModel): Observable<PageModel<UomCategoryModel>>  {
    console.log(this.navbarMenuStore.selectedLanguage())
    return this.http.get<PageModel<UomCategoryModel>>(
      `${this.apiUrl}/catalog/uom-categories`,
      {
          headers: this.getHttpHeader(),
          params: {...findParamModel}
        }
      )
      .pipe(catchError(this.handleError));
  };
}
