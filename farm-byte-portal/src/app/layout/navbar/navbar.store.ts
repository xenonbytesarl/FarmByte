import {patchState, signalStore, withMethods, withState} from "@ngrx/signals";

type NavbarState = {
  toggleSidebar: boolean;
  showSearchInput: boolean;
}

const initialState: NavbarState = {
  toggleSidebar: false,
  showSearchInput: false,
}

export const NavBarStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => ({
    doToggleSidebar(): void {
      patchState(store, (state) => ({
        ...state,
        toggleSidebar: !state.toggleSidebar
      }))
    },
    doShowSearchInput(): void {
      patchState(store, (state) => ({
        ...state,
        showSearchInput: !state.showSearchInput
      }))
    },
    doToggleMobilebar(): void {}
  }))
)
