import {NavbarMenuModel} from "@/layouts/model/navbar-menu-model.ts";
import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {LanguageModel} from "@/layouts/model/language.ts";


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

export const {changeLanguage, updateNavbarMenus} = navbarSlice.actions;
export default navbarSlice.reducer;