import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {sidebarMenus} from "@/layouts/data/sidebar-menu-data.ts";
import {RootState} from "@/store/store.ts";


interface SidebarState {
    sidebarMenus: SidebarMenuModel[];
    selectedSidebarMenu?: SidebarMenuModel;
    isOpenSidebarMenu: boolean;
}

const initialState: SidebarState = {
    sidebarMenus: sidebarMenus,
    selectedSidebarMenu: sidebarMenus[0],
    isOpenSidebarMenu: true
};

const sidebarSlice = createSlice({
    name: "sidebarMenu",
    initialState,
    reducers: {
        selectSidebarMenu: (state, action: PayloadAction<SidebarMenuModel>) => {
            state.selectedSidebarMenu = action.payload;
        },
        openSidebarMenu: (state, action: PayloadAction<boolean>) => {
            state.isOpenSidebarMenu = !action.payload;
        }
    }
});

export const selectSidebarMenus = (state: RootState) => state.sidebar.sidebarMenus;
export const selectSelectedSidebarMenu = (state: RootState) => state.sidebar.selectedSidebarMenu;
export const selectIsOpenSidebarMenu = (state: RootState) => state.sidebar.isOpenSidebarMenu;

export const {selectSidebarMenu, openSidebarMenu} = sidebarSlice.actions;

export default sidebarSlice.reducer;