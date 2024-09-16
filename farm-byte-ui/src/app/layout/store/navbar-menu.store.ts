import {NavbarMenu} from "../models/navbar-menu.model";
import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";
import {inject} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";

export type Language = 'en' | 'fr';

type NavbarMenuState = {
  navbarMenus: NavbarMenu[];
  selectedModule: string;
  selectedLanguage: Language;
}

const initialState: NavbarMenuState = {
  navbarMenus: [],
  selectedModule: '',
  selectedLanguage: 'fr'
};

export const NavbarMenuStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store, translateService = inject(TranslateService)) => ({
    loadNavbarMenus: (currentNavbarMenus: NavbarMenu[], currentModule: string): void => {
      patchState(store, (state) => ({
        ...state,
        navbarMenus: currentNavbarMenus,
        selectedModule: currentModule
      }));
    },
    changeLanguage: (currentLanguage: Language): void => {
      translateService.use(currentLanguage);
      patchState(store, (state) => ({
        ...state,
        selectedLanguage: currentLanguage
      }));
    }
  }))
)
