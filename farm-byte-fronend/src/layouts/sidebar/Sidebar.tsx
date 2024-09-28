import React from "react";
import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import SidebarMenu from "@/layouts/sidebar/SidebarMenu.tsx";
import {useDispatch, useSelector} from "react-redux";
import {RootDispatch, RootState} from "@/store/store.ts";
import {openSidebarMenu} from "@/layouts/state/sidebar-menu-slice.ts";

const Sidebar = () => {
    const sidebarMenus = useSelector((state: RootState) => state.sidebar.sidebarMenus);
    const isOpen = useSelector((state: RootState) => state.sidebar.isOpenSidebarMenu);
    const dispatch = useDispatch<RootDispatch>();


    const toggleSidebar =  (event: React.MouseEvent<HTMLDivElement>) => {
        event.preventDefault();
        dispatch(openSidebarMenu(isOpen));
    }

    return (
        <div className={`flex flex-col bg-amber-800 min-h-screen py-3 text-white ${isOpen? 'w-80 transition-all ease-linear duration-300': 'w-20 transition-all ease-linear duration-300'}`}>
            <div className="relative flex flex-row justify-between items-center px-5 mb-5 w-full">
                <div className={`flex flex-row justify-between items-center w-[95%]`}>
                    <p className={` ${isOpen ? 'block text-xl font-medium' : 'hidden text-2xl text-center'}`}>FarmByte.cm</p>
                </div>
                <p onClick={(event) => toggleSidebar(event)}
                   className={`absolute -right-3.5 material-symbols-outlined text-white cursor-pointer p-1 bg-amber-600 rounded-full ${isOpen ? '' : 'mt-8'}`}>{isOpen ? 'arrow_back' : 'arrow_forward'}</p>
            </div>
            <div className="flex flex-col justify-start items-start text-lg">
                {
                    sidebarMenus.map((sidebarMenu : SidebarMenuModel) => (
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