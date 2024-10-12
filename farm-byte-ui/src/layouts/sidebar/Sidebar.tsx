import React from "react";
import {useDispatch, useSelector} from "react-redux";
import SidebarMenu from "@/layouts/sidebar/SidebarMenu.tsx";
import {SidebarMenuModel} from "@/layouts/model/SidebarMenuModel.ts";
import {RootDispatch} from "@/Store.ts";
import {openSidebarMenu, selectIsOpenSidebarMenu, selectSidebarMenus} from "@/layouts/sidebar/SidebarMenuSlice.ts";

const Sidebar = () => {

    const sidebarMenus: SidebarMenuModel[] = useSelector(selectSidebarMenus);
    const isOpen: boolean = useSelector(selectIsOpenSidebarMenu);
    const dispatch = useDispatch<RootDispatch>();


    const toggleSidebar =  (event: React.MouseEvent<HTMLDivElement>) => {
        event.preventDefault();
        dispatch(openSidebarMenu(isOpen));
    }

    return (
        <div
            className={`fixed flex flex-col bg-gradient-to-r from-primary/80 to-primary/60  h-screen py-3 text-primary-foreground z-30 ${isOpen ? 'w-72 transition-all ease-linear duration-300' : 'w-24 transition-all ease-linear duration-300'}`}>
            <div className="relative flex flex-row justify-between items-center px-5 pt-4 mb-12 w-full">
                <div className={`flex flex-row justify-between items-center w-[95%]`}>
                    <p className={` text-center ${isOpen ? 'block text-2xl font-medium' : 'text-xl'}`}>{isOpen ? 'FarmByte.cm' : 'FB.cm'}</p>
                </div>
                <p
                    className={`absolute -right-3.5 top-[3.2rem] material-symbols-outlined text-primary-foreground cursor-pointer p-1 bg-white rounded-full z-30 ${isOpen ? '' : ''}`}>&nbsp;</p>
                <p onClick={(event) => toggleSidebar(event)}
                   className={`absolute -right-3 top-[3.35rem] material-symbols-outlined text-primary-foreground cursor-pointer p-0.5 bg-gradient-to-r from-primary to-primary rounded-full z-40 ${isOpen ? '' : ''}`}>{isOpen ? 'arrow_back' : 'arrow_forward'}</p>
            </div>
            <div className="flex flex-col justify-start items-start text-lg">
                {
                    sidebarMenus.map((sidebarMenu: SidebarMenuModel) => (
                        <SidebarMenu key={sidebarMenu.label} isOpen={isOpen} sidebarMenu={sidebarMenu}/>
                    ))
                }
            </div>
            <div
                className={`flex flex-row justify-center items-end mt-auto mx-auto border-t-2 py-5 w-[95%] border-primary ${isOpen ? 'block' : 'hidden'}`}>
                <span>copyright    &#169;2024</span>
            </div>
        </div>
    );
}

export default Sidebar;