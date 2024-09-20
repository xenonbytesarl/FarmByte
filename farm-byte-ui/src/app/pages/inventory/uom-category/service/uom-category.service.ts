import {Injectable} from '@angular/core';
import {catchError, Observable} from "rxjs";
import {GlobalService} from "../../../../core/services/global.service";
import {PageModel} from "../../../../core/model/page.model";
import {UomCategoryModel} from "../model/uom-category.model";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {SearchParamModel} from "../../../../core/model/search-param.model";

@Injectable({
  providedIn: 'root'
})
export class UomCategoryService extends GlobalService {

  findUomCategories$ =  (findParamModel: FindParamModel) => <Observable<SuccessResponseModel<PageModel<UomCategoryModel>>>>

    this.http.get<SuccessResponseModel<PageModel<UomCategoryModel>>>(
      `${this.apiUrl}/catalog/uom-categories`,
      {
          headers: this.getHttpHeader(),
          params: {...findParamModel}
        }
      )
      .pipe(catchError(this.handleError));


  searchUomCategories$ =  (searchParamModel: SearchParamModel) => <Observable<SuccessResponseModel<PageModel<UomCategoryModel>>>>

    this.http.get<SuccessResponseModel<PageModel<UomCategoryModel>>>(
      `${this.apiUrl}/catalog/uom-categories/search`,
      {
        headers: this.getHttpHeader(),
        params: {...searchParamModel}
      }
    )
      .pipe(catchError(this.handleError));

  findUomCategoryById$ =  (id: string) => <Observable<SuccessResponseModel<UomCategoryModel>>>

    this.http.get<SuccessResponseModel<UomCategoryModel>>(
      `${this.apiUrl}/catalog/uom-categories/${id}`,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));

  createUomCategory$ =  (uomCategory: UomCategoryModel) => <Observable<SuccessResponseModel<UomCategoryModel>>>

    this.http.post<SuccessResponseModel<UomCategoryModel>>(
      `${this.apiUrl}/catalog/uom-categories`, uomCategory,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));

  updateUomCategory$ =  (uomCategory: UomCategoryModel) => <Observable<SuccessResponseModel<UomCategoryModel>>>

    this.http.put<SuccessResponseModel<UomCategoryModel>>(
      `${this.apiUrl}/catalog/uom-categories/${uomCategory.id}`, uomCategory,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));
}
