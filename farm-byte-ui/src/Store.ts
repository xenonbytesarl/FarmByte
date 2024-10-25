import {configureStore} from "@reduxjs/toolkit";
import sidebarSlice from "@/layouts/sidebar/SidebarMenuSlice.ts";
import navbarSlice from "@/layouts/navbar/NavbarMenuSlice.ts";
import {stockSlice} from "@/pages/stock/StockSlice.ts";

export const store = configureStore({
    reducer: {
        sidebar: sidebarSlice,
        navbar: navbarSlice,
        stock: stockSlice,
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type RootDispatch = typeof store.dispatch;