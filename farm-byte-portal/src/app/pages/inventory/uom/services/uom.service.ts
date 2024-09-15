import { Injectable } from '@angular/core';
import {GlobalService} from "../../../../core/services/global.service";
import {FindParamModel} from "../../../../core/model/find-param.model";
import {catchError, Observable} from "rxjs";
import {SuccessResponseModel} from "../../../../core/model/success-response.model";
import {PageModel} from "../../../../core/model/page.model";
import {UomModel} from "../model/uom.model";

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
}
