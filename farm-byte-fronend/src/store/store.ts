import {configureStore} from "@reduxjs/toolkit";
import sidebarSlice from "@/layouts/state/sidebar-menu-slice.ts";
import navbarSlice from "@/layouts/state/navbar-menu-slice.ts";

export const store = configureStore({
    reducer: {
        sidebar: sidebarSlice,
        navbar: navbarSlice,
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type RootDispatch = typeof store.dispatch;