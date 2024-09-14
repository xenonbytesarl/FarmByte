import {inject, Injectable} from '@angular/core';
import {ConfigInitService} from "../../../core/config-init.service";
import {environment} from "../../../../environments/environment";
import {catchError, Observable, of} from "rxjs";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class CategoryOfUnitOfMeasureService {
  private configInit: ConfigInitService = inject(ConfigInitService);
  private http: HttpClient = inject(HttpClient);
  private apiUrl: string = this.configInit.getConfigValue('API_BACKEND_URL') as string;

  getUomCategories (): Observable<any[]>  {
    return this.http.get<any[]>(`${this.apiUrl}/catalog/uom-categories`);
  };
}
