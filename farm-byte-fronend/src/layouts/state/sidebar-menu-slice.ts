import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {sidebarMenus} from "@/layouts/data/sidebar-menu-data.ts";


interface NavbarMenuState {
    sidebarMenus: SidebarMenuModel[];
    selectedSidebarMenu?: SidebarMenuModel;
}

const initialState: NavbarMenuState = {
    sidebarMenus: sidebarMenus,
    selectedSidebarMenu: sidebarMenus[0]
};

const sidebarMenuSlice = createSlice({
    name: "sidebarMenu",
    initialState,
    reducers: {
        selectSidebarMenu: (state, action: PayloadAction<SidebarMenuModel>) => {
            console.log('action', action);
            state.selectedSidebarMenu = action.payload;
        }
    }
});

export const {selectSidebarMenu} = sidebarMenuSlice.actions;

export default sidebarMenuSlice.reducer;