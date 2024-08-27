import {SidebarMenu} from "./menu.model";
import {sidebarMenus} from "./menu.data";
import {patchState, signalStore, withHooks, withMethods, withState} from "@ngrx/signals";

type SidebarState = {
  sidebarMenus: SidebarMenu[];
  selectedSidebarMenu: SidebarMenu | undefined;
}

const initialState: SidebarState = {
  sidebarMenus: [],
  selectedSidebarMenu: undefined
}

export const SidebarStore = signalStore(
  {providedIn: 'root'},
  withState(initialState),
  withMethods((store) => ({
    loadSidebarMenu(): void {
      patchState(store, (state) => ({
        ...state,
        sidebarMenus: sidebarMenus
      }))
    },
    changeSidebarMenus(sidebarMenu: SidebarMenu | undefined) {
      if(sidebarMenu) {
        patchState(store, (state) => ({
          ...state,
          selectedSidebarMenu: sidebarMenu
        }))
      }
    }
  })),
  withHooks((store) => ({
    onInit(): void {
      store.loadSidebarMenu();
      store.changeSidebarMenus(sidebarMenus[1]);
    }
  }))
)
