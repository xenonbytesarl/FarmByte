import {NavMenu, navMenuItems} from "./nav-menu.model";
import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";

type NavMenuState = {
  navMenus: NavMenu[];
}

const initialState: NavMenuState = {
  navMenus: []
};

export const NavMenuStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => ({
    loadNavMenus: (): void => {
      patchState(store, (state) => ({
        ...state,
        navMenus: navMenuItems
      }))
    },
  })),
  withHooks({
    onInit: (store): void => {
      store.loadNavMenus();
    }
  })
)
