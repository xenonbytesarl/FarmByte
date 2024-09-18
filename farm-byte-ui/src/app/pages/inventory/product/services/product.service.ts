import { Injectable } from '@angular/core';
import {GlobalService} from "../../../../core/services/global.service";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {catchError, Observable} from "rxjs";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {ProductModel} from "../model/product.model";
import {SearchParamModel} from "../../../../core/model/search-param.model";

@Injectable({
  providedIn: 'root'
})
export class ProductService extends GlobalService{
  findProducts$ = (findParamModel: FindParamModel) => <Observable<SuccessResponseModel<PageModel<ProductModel>>>>
    this.http.get<SuccessResponseModel<PageModel<ProductModel>>>(
      `${this.apiUrl}/catalog/products`,
      {
        headers: this.getHttpHeader(),
        params: {...findParamModel}
      }
    )
      .pipe(catchError(this.handleError));

  searchProducts$ = (searchParamModel: SearchParamModel) => <Observable<SuccessResponseModel<PageModel<ProductModel>>>>
    this.http.get<SuccessResponseModel<PageModel<ProductModel>>>(
      `${this.apiUrl}/catalog/products/search`,
      {
        headers: this.getHttpHeader(),
        params: {...searchParamModel}
      }
    )
      .pipe(catchError(this.handleError));


  findProductById$ =  (id: string) => <Observable<SuccessResponseModel<ProductModel>>>

    this.http.get<SuccessResponseModel<ProductModel>>(
      `${this.apiUrl}/catalog/products/${id}`,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));
}
