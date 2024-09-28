import React from "react";
import {useDispatch} from "react-redux";
import {useNavigate} from "react-router-dom";
import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {RootDispatch} from "@/store/store.ts";
import {selectSidebarMenu} from "@/layouts/state/sidebar-menu-slice.ts";

type Props = {
    sidebarMenu: SidebarMenuModel,
    isOpen: boolean,
}

const SidebarMenu = ({sidebarMenu, isOpen}: Props) => {

    const dispatch = useDispatch<RootDispatch>();
    const navigate = useNavigate();

    const handleSelectSidebarMenu = (_event: React.MouseEvent<HTMLDivElement>, sidebarMenu: SidebarMenuModel) => {
        _event.preventDefault();
        dispatch(selectSidebarMenu(sidebarMenu));
        navigate(sidebarMenu.link);
    }

    return (
        <div onClick={(event) => handleSelectSidebarMenu(event, sidebarMenu)}
              className={`flex flex-row ${isOpen ? 'justify-start' : 'justify-center'} items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out`}>
            <span className="material-symbols-outlined">{sidebarMenu.icon}</span>
            <p className={`${isOpen ? 'transition-all ease-out duration-300 opacity-100' : 'hidden transition-all ease-in duration-300 opacity-0'}`}>{sidebarMenu.label}</p>
        </div>
    );
}


export default SidebarMenu;