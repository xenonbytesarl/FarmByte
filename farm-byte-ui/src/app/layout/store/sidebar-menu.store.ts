import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";
import {SidebarMenu} from "../models/sidebar-menu.model";
import {sidebarMenuItems} from "../data/sidebar-menu.data";

type SidebarMenuState = {
  sidebarMenus: SidebarMenu[];
  selectedSidebarMenu: SidebarMenu | undefined;
}

const initialState: SidebarMenuState = {
  sidebarMenus: [],
  selectedSidebarMenu: undefined
};

export const SidebarMenuStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => ({
    loadSidebarMenus: (): void => {
      patchState(store, (state) => ({
        ...state,
        sidebarMenus: sidebarMenuItems
      }))
    },
    updateCurrentSidebarMenu(currentSidebarMenu: SidebarMenu): void {
      patchState(store, (state) => ({
        ...state,
        selectedSidebarMenu: currentSidebarMenu
      }))
    }
  })),
  withHooks({
    onInit: (store): void => {
      store.loadSidebarMenus();
    }
  })
)
