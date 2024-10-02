import {configureStore} from "@reduxjs/toolkit";
import sidebarSlice from "@/layouts/state/sidebar-menu-slice.ts";
import navbarSlice from "@/layouts/state/navbar-menu-slice.ts";
import {inventorySlice} from "@/pages/inventory/InventorySlice.ts";

export const store = configureStore({
    reducer: {
        sidebar: sidebarSlice,
        navbar: navbarSlice,
        inventory: inventorySlice,
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type RootDispatch = typeof store.dispatch;