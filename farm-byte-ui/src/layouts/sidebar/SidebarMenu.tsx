import React from "react";
import {useSelector} from "react-redux";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";

import {SidebarMenuModel} from "@/layouts/model/SidebarMenuModel.ts";
import {selectSelectedSidebarMenu} from "@/layouts/sidebar/SidebarMenuSlice.ts";

type Props = {
    sidebarMenu: SidebarMenuModel,
    isOpen: boolean,
}

const SidebarMenu = ({sidebarMenu, isOpen}: Props) => {

    const {t} = useTranslation(['home']);

    const selectedSidebarMenu: SidebarMenuModel | undefined = useSelector(selectSelectedSidebarMenu);

    const navigate = useNavigate();

    const handleSelectSidebarMenu = (_event: React.MouseEvent<HTMLDivElement>, sidebarMenu: SidebarMenuModel) => {
        _event.preventDefault();
        navigate(sidebarMenu.link);
    }

    return (
        <div onClick={(event) => handleSelectSidebarMenu(event, sidebarMenu)}
              className={`flex flex-row ${isOpen ? 'justify-start' : 'justify-center'} ${selectedSidebarMenu?.link === sidebarMenu.link? 'bg-gradient-to-r from-primary-foreground/50 to-primary-foreground/50 text-primary': 'text-primary-foreground'} items-center gap-3 px-3 py-5 cursor-pointer w-full  hover:bg-gradient-to-r hover:from-primary-foreground/50 hover:to-primary-foreground/50 hover:text-primary hover:transition hover:duration-300 hover:ease-in-out`}>
            <span className="material-symbols-outlined">{sidebarMenu.icon}</span>
            <p className={`${isOpen ? 'transition-all ease-out duration-300 opacity-100' : 'hidden transition-all ease-in duration-300 opacity-0'}`}>{t(sidebarMenu.label)}</p>
        </div>
    );
}


export default SidebarMenu;