import {useNavigate} from "react-router-dom";
import React from "react";
import {useTranslation} from "react-i18next";
import {NavbarMenuItemModel} from "@/layouts/model/navbar-menu-item-model.ts";

type Props = {
    navbarMenuItem: NavbarMenuItemModel,
    isOpenSubMenu: boolean,
}

const NavbarMenuItem = ({navbarMenuItem, isOpenSubMenu}: Props) => {
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
        <div  onClick={(event) => handleOnclick(event, navbarMenuItem)} className="block text-gray-500 p-2 text-sm hover:text-white hover:bg-amber-700 hover:transition-all hover:ease-in-out hover:duration-300 ">
            {t(navbarMenuItem.label)}
        </div>
    );
}

export default NavbarMenuItem;