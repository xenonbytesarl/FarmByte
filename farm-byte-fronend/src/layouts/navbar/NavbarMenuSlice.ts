import {NavbarMenuModel} from "@/layouts/model/NavbarMenuModel.ts";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {LanguageModel} from "@/layouts/model/LanguageModel.ts";
import {RootState} from "@/Store.ts";


interface NavbarState {
    navbarMenus: NavbarMenuModel[];
    language: LanguageModel;
}

const initialState: NavbarState = {
    navbarMenus: [],
    language: {
        name: 'en'
    }
}

const navbarSlice = createSlice({
    name: "navbar",
    initialState,
    reducers: {
        changeLanguage: (state, action: PayloadAction<LanguageModel>) => {
            state.language = action.payload;
        },
        updateNavbarMenus: (state, action: PayloadAction<NavbarMenuModel[]>) => {
            state.navbarMenus = action.payload;
        }
    }
});

export const selectLanguage = (state: RootState) => state.navbar.language;
export const selectNavbarMenus = (state: RootState) => state.navbar.navbarMenus;

export const {changeLanguage, updateNavbarMenus} = navbarSlice.actions;
export default navbarSlice.reducer;