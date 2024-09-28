import {configureStore} from "@reduxjs/toolkit";
import sidebarMenuSlice from "@/layouts/state/sidebar-menu-slice.ts";

export const store = configureStore({
    reducer: {
        sidebar: sidebarMenuSlice,
    }
});

export type RootState = ReturnType<typeof store.getState>;
export type RootDispatch = typeof store.dispatch;