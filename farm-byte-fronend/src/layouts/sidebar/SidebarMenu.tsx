import React from "react";
import {useDispatch, useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {RootDispatch, RootState} from "@/store/store.ts";
import {selectSidebarMenu} from "@/layouts/state/sidebar-menu-slice.ts";
import {updateNavbarMenus} from "@/layouts/state/navbar-menu-slice.ts";

type Props = {
    sidebarMenu: SidebarMenuModel,
    isOpen: boolean,
}

const SidebarMenu = ({sidebarMenu, isOpen}: Props) => {
    const {t} = useTranslation(['home']);
    const dispatch = useDispatch<RootDispatch>();
    const selectedSidebarMenu = useSelector((state: RootState) => state.sidebar.selectedSidebarMenu);
    const navigate = useNavigate();

    const handleSelectSidebarMenu = (_event: React.MouseEvent<HTMLDivElement>, sidebarMenu: SidebarMenuModel) => {
        _event.preventDefault();
        dispatch(selectSidebarMenu(sidebarMenu));
        if(sidebarMenu.navbarMenu) {
            dispatch(updateNavbarMenus(sidebarMenu.navbarMenu));
        }
        navigate(sidebarMenu.link);
    }

    return (
        <div onClick={(event) => handleSelectSidebarMenu(event, sidebarMenu)}
              className={`flex flex-row ${isOpen ? 'justify-start' : 'justify-center'} ${selectedSidebarMenu?.link === sidebarMenu.link? 'bg-amber-900/40 text-amber-500': ''} items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out`}>
            <span className="material-symbols-outlined">{sidebarMenu.icon}</span>
            <p className={`${isOpen ? 'transition-all ease-out duration-300 opacity-100' : 'hidden transition-all ease-in duration-300 opacity-0'}`}>{t(sidebarMenu.label)}</p>
        </div>
    );
}


export default SidebarMenu;