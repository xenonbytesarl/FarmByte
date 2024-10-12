import {useNavigate} from "react-router-dom";
import React from "react";
import {useTranslation} from "react-i18next";
import {NavbarMenuItemModel} from "@/layouts/model/NavbarMenuItemModel.ts";

type Props = {
    navbarMenuItem: NavbarMenuItemModel,
    isOpenSubMenu: boolean,
    lastNavMenuItem: NavbarMenuItemModel | undefined,
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
              className={`block text-secondary-500 p-2 text-sm rounded-b-lg ${navbarMenuItem.label !== lastNavMenuItem?.label? 'rounded-b-none': ''} hover:text-primary-foreground hover:bg-gradient-to-r hover:from-primary hover:to-primary hover:transition-all hover:ease-in-out hover:duration-300 `}>
            {t(navbarMenuItem.label)}
        </div>
    );
}

export default NavbarMenuItem;