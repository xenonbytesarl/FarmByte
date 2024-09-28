import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {sidebarMenus} from "@/layouts/data/sidebar-menu-data.ts";


interface NavbarMenuState {
    sidebarMenus: SidebarMenuModel[];
    selectedSidebarMenu?: SidebarMenuModel;
    isOpen: boolean;
}

const initialState: NavbarMenuState = {
    sidebarMenus: sidebarMenus,
    selectedSidebarMenu: sidebarMenus[0],
    isOpen: true
};

const sidebarMenuSlice = createSlice({
    name: "sidebarMenu",
    initialState,
    reducers: {
        selectSidebarMenu: (state, action: PayloadAction<SidebarMenuModel>) => {
            state.selectedSidebarMenu = action.payload;
        },
        openSidebarMenu: (state, action: PayloadAction<boolean>) => {
            state.isOpen = !action.payload;
        }
    }
});

export const {selectSidebarMenu, openSidebarMenu} = sidebarMenuSlice.actions;

export default sidebarMenuSlice.reducer;