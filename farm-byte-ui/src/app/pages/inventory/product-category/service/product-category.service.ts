import {Injectable} from '@angular/core';
import {FindParamModel} from "../../../../core/model/find-param.model";
import {catchError, Observable} from "rxjs";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {ProductCategoryModel} from "../model/product-category.model";
import {GlobalService} from "../../../../core/services/global.service";
import {SearchParamModel} from "../../../../core/model/search-param.model";

@Injectable({
  providedIn: 'root'
})
export class ProductCategoryService extends GlobalService{

  findProductCategories$ =  (findParamModel: FindParamModel) => <Observable<SuccessResponseModel<PageModel<ProductCategoryModel>>>>

    this.http.get<SuccessResponseModel<PageModel<ProductCategoryModel>>>(
      `${this.apiUrl}/catalog/product-categories`,
      {
        headers: this.getHttpHeader(),
        params: {...findParamModel}
      }
    )
      .pipe(catchError(this.handleError));

  searchProductCategories$ =  (searchParamModel: SearchParamModel) => <Observable<SuccessResponseModel<PageModel<ProductCategoryModel>>>>

    this.http.get<SuccessResponseModel<PageModel<ProductCategoryModel>>>(
      `${this.apiUrl}/catalog/product-categories/search`,
      {
        headers: this.getHttpHeader(),
        params: {...searchParamModel}
      }
    )
      .pipe(catchError(this.handleError));

  findProductCategoryById$ =  (id: string) => <Observable<SuccessResponseModel<ProductCategoryModel>>>

    this.http.get<SuccessResponseModel<ProductCategoryModel>>(
      `${this.apiUrl}/catalog/product-categories/${id}`,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));
}
