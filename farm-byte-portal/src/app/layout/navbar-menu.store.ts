import {NavbarMenu, inventoryNavbarMenus} from "./navbar-menu.model";
import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";

type NavbarMenuState = {
  navbarMenus: NavbarMenu[];
  module: string;
}

const initialState: NavbarMenuState = {
  navbarMenus: [],
  module: ''
};

export const NavbarMenuStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => ({
    loadNavbarMenus: (currentNavbarMenu: NavbarMenu[], currentModule: string): void => {
      patchState(store, (state) => ({
        ...state,
        navbarMenus: currentNavbarMenu,
        module: currentModule
      }))
    },
  }))/*,
  withHooks({
    onInit: (store): void => {
      store.loadNavMenus();
    }
  })*/
)
