import React from "react";
import {useDispatch, useSelector} from "react-redux";
import SidebarMenu from "@/layouts/sidebar/SidebarMenu.tsx";
import {SidebarMenuModel} from "@/layouts/model/sidebarMenuModel.ts";
import {RootDispatch} from "@/store/store.ts";
import {openSidebarMenu, selectIsOpenSidebarMenu, selectSidebarMenus} from "@/layouts/state/sidebar-menu-slice.ts";

const Sidebar = () => {

    const sidebarMenus: SidebarMenuModel[] = useSelector(selectSidebarMenus);
    const isOpen: boolean = useSelector(selectIsOpenSidebarMenu);
    const dispatch = useDispatch<RootDispatch>();


    const toggleSidebar =  (event: React.MouseEvent<HTMLDivElement>) => {
        event.preventDefault();
        dispatch(openSidebarMenu(isOpen));
    }

    return (
        <div className={`flex flex-col bg-gradient-to-r from-amber-800 to-amber-400  min-h-screen py-3 text-white ${isOpen? 'w-80 transition-all ease-linear duration-300': 'w-24 transition-all ease-linear duration-300'}`}>
            <div className="relative flex flex-row justify-between items-center px-5 pt-4 mb-12 w-full">
                <div className={`flex flex-row justify-between items-center w-[95%]`}>
                    <p className={` text-center ${isOpen ? 'block text-2xl font-medium' : 'text-xl'}`}>{isOpen? 'FarmByte.cm': 'FB.cm'}</p>
                </div>
                <p
                   className={`absolute -right-3.5 top-[3.2rem] material-symbols-outlined text-white cursor-pointer p-1 bg-white rounded-full ${isOpen ? '' : ''}`}>&nbsp;</p>
                <p onClick={(event) => toggleSidebar(event)}
                   className={`absolute -right-3 top-[3.35rem] material-symbols-outlined text-white cursor-pointer p-0.5 bg-gradient-to-r from-amber-800 to-amber-400 rounded-full ${isOpen ? '' : ''}`}>{isOpen ? 'arrow_back' : 'arrow_forward'}</p>
            </div>
            <div className="flex flex-col justify-start items-start text-lg">
                {
                    sidebarMenus.map((sidebarMenu: SidebarMenuModel) => (
                        <SidebarMenu key={sidebarMenu.label} isOpen={isOpen} sidebarMenu={sidebarMenu} />
                    ))
                }
            </div>
            <div className={`flex flex-row justify-center items-end mt-auto mx-auto border-t-2 py-5 w-[95%] border-amber-700 ${isOpen? 'block': 'hidden'}`}>
                <span>copyright    &#169;2024</span>
            </div>
        </div>
    );
}

export default Sidebar;