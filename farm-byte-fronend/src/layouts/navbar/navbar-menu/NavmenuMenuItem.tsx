import {useNavigate} from "react-router-dom";
import React from "react";
import {useTranslation} from "react-i18next";
import {NavbarMenuItemModel} from "@/layouts/model/navbarMenuItemModel.ts";

type Props = {
    navbarMenuItem: NavbarMenuItemModel,
    isOpenSubMenu: boolean,
    lastNavMenuItem: NavbarMenuItemModel,
}

const NavbarMenuItem = ({navbarMenuItem, isOpenSubMenu, lastNavMenuItem}: Props) => {

    const {t} = useTranslation(['home']);

    const navigate = useNavigate();

    const handleOnclick = (event: React.MouseEvent<HTMLDivElement>, navbarMenuItem: NavbarMenuItemModel) => {
        //TODO continue to find how to hide sub menu
        event.preventDefault();
       if(isOpenSubMenu) {
           navigate(navbarMenuItem.link);
       }
    }

    return (
        <div  onClick={(event) => handleOnclick(event, navbarMenuItem)}
              className={`block text-gray-500 p-2 text-sm rounded-b-lg ${navbarMenuItem.label !== lastNavMenuItem.label? 'rounded-b-none': ''} hover:text-white hover:bg-gradient-to-r hover:from-amber-800 hover:to-amber-400 hover:transition-all hover:ease-in-out hover:duration-300 `}>
            {t(navbarMenuItem.label)}
        </div>
    );
}

export default NavbarMenuItem;