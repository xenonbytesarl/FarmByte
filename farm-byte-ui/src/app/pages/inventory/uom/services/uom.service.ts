import {Injectable} from '@angular/core';
import {GlobalService} from "../../../../core/services/global.service";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {catchError, Observable} from "rxjs";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {UomModel} from "../model/uom.model";
import {SearchParamModel} from "../../../../core/model/search-param.model";

@Injectable({
  providedIn: 'root'
})
export class UomService extends GlobalService {
  findUoms$ = (findParamModel: FindParamModel) => <Observable<SuccessResponseModel<PageModel<UomModel>>>>
    this.http.get<SuccessResponseModel<PageModel<UomModel>>>(
      `${this.apiUrl}/catalog/uoms`,
      {
        headers: this.getHttpHeader(),
        params: {...findParamModel}
      }
    )
    .pipe(catchError(this.handleError));

  searchUoms$ = (searchParamModel: SearchParamModel) => <Observable<SuccessResponseModel<PageModel<UomModel>>>>
    this.http.get<SuccessResponseModel<PageModel<UomModel>>>(
      `${this.apiUrl}/catalog/uoms/search`,
      {
        headers: this.getHttpHeader(),
        params: {...searchParamModel}
      }
    )
      .pipe(catchError(this.handleError));


  findUomById$ =  (id: string) => <Observable<SuccessResponseModel<UomModel>>>

    this.http.get<SuccessResponseModel<UomModel>>(
      `${this.apiUrl}/catalog/uoms/${id}`,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));

  createUom$ =  (uom: UomModel) => <Observable<SuccessResponseModel<UomModel>>>

    this.http.post<SuccessResponseModel<UomModel>>(
      `${this.apiUrl}/catalog/uoms`, uom,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));

  updateUom$ =  (uom: UomModel) => <Observable<SuccessResponseModel<UomModel>>>

    this.http.put<SuccessResponseModel<UomModel>>(
      `${this.apiUrl}/catalog/uoms/${uom.id}`, uom,
      {
        headers: this.getHttpHeader()
      }
    )
      .pipe(catchError(this.handleError));
}
