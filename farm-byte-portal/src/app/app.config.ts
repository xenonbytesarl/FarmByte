import {APP_INITIALIZER, ApplicationConfig, importProvidersFrom, provideZoneChangeDetection} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {ConfigInitService} from "./core/services/config-init.service";
import {take} from "rxjs";

const httpLoaderFactory: (http: HttpClient) => TranslateHttpLoader = (http: HttpClient) =>
  new TranslateHttpLoader(http, './assets/i18n/', '.json');

const configInit: () => ConfigInitService = () => new ConfigInitService();


export const appConfig: ApplicationConfig = {
  providers: [
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(),
    provideRouter(routes),
    importProvidersFrom([
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useFactory: httpLoaderFactory,
          deps: [HttpClient],
        },
      })
    ]),
    {
      provide: APP_INITIALIZER,
      useFactory: (configInit: ConfigInitService) => {
        return () => {
          return configInit.loadConfig().pipe(take(1));
        }
      },
      multi: true,
      deps: [ConfigInitService],
    }
  ]
};
