import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ConfigInitService} from "./config-init.service";
import {Observable, throwError} from "rxjs";
import {NavbarMenuStore} from "../../layout/store/navbar-menu.store";

@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  protected configInit: ConfigInitService = inject(ConfigInitService);
  protected http: HttpClient = inject(HttpClient);
  protected apiUrl: string = this.configInit.getConfigValue('API_BACKEND_URL') as string;
  protected navbarMenuStore = inject(NavbarMenuStore);

  handleError(httpErrorResponse: HttpErrorResponse): Observable<any> {
    let errorMessage: any;
    if(httpErrorResponse.error instanceof ErrorEvent) {
      errorMessage = `A client error occurred - ${httpErrorResponse.error}`;
    } else {
      if(httpErrorResponse.error && httpErrorResponse.error.error) {
        errorMessage = httpErrorResponse.error.error;
      } else if(httpErrorResponse.error && httpErrorResponse.error.reason) {
        errorMessage = httpErrorResponse.error.reason;
      } else  {
        errorMessage = `An error occurred - Error status ${httpErrorResponse.status}`;
      }
    }
    return throwError(() => errorMessage);
  }

  getHttpHeader(): any {
    return {'Accept-Language': this.navbarMenuStore.selectedLanguage() };
  }

}
