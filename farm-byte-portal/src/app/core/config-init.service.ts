import {inject, Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, mergeMap, Observable, of} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class ConfigInitService {

  private config: any;
  readonly http: HttpClient = inject(HttpClient);

  public loadConfig(): Observable<any> {
    return this.http.get(
      this.getConfigFile(),{observe: 'response'}
    ).pipe(
      catchError((error) => {
        console.error(error);
        return of(null);
      }),
      mergeMap((response) => {
        if(response && response.body) {
          this.config = response.body;
          return of(this.config);
        } else {
          return of(null);
        }
      })
    );
  }

  public getConfig(): any {
    return this.config;
  }

  public getConfigValue(key: string): any {
    return this.config? this.config[key]: null;
  }

  private getConfigFile(): string {
    return environment.configFile;
  }
}
