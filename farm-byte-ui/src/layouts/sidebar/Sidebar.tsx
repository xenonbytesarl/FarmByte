import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useLocation} from "react-router-dom";
import SidebarMenu from "@/layouts/sidebar/SidebarMenu.tsx";
import {SidebarMenuModel} from "@/layouts/model/SidebarMenuModel.ts";
import {RootDispatch} from "@/Store.ts";
import {
    openSidebarMenu,
    selectIsOpenSidebarMenu, selectSelectedSidebarMenu,
    selectSidebarMenu,
    selectSidebarMenus
} from "@/layouts/sidebar/SidebarMenuSlice.ts";
import {updateNavbarMenus} from "@/layouts/navbar/NavbarMenuSlice.ts";

const Sidebar = () => {

    const location = useLocation();

    const sidebarMenus: SidebarMenuModel[] = useSelector(selectSidebarMenus);
    const selectedSidebarMenu: SidebarMenuModel | undefined = useSelector(selectSelectedSidebarMenu);
    const isOpen: boolean = useSelector(selectIsOpenSidebarMenu);
    const dispatch = useDispatch<RootDispatch>();

    useEffect(() => {
        console.log(location.pathname);
        dispatch(selectSidebarMenu("/" + location.pathname.split("/")[1]));
    }, [location])

    useEffect(() => {
        if(selectedSidebarMenu?.navbarMenu) {
            dispatch(updateNavbarMenus(selectedSidebarMenu.navbarMenu));
        } else {
            dispatch(updateNavbarMenus([]));
        }
    }, [selectedSidebarMenu]);


    const toggleSidebar =  (event: React.MouseEvent<HTMLDivElement>) => {
        event.preventDefault();
        dispatch(openSidebarMenu(isOpen));
    }

    return (
        <div
            className={`fixed flex flex-col bg-gradient-to-r from-primary/100 to-primary/100  h-screen py-2 text-primary-foreground z-30 ${isOpen ? 'w-72 transition-all ease-linear duration-300' : 'w-24 transition-all ease-linear duration-300'}`}>
            <div className="relative flex flex-row justify-between items-center p-5 mb-12 w-full shadow-lg">
                <div className={`flex flex-row justify-between items-center w-[95%]`}>
                    <p className={` text-center ${isOpen ? 'block text-2xl font-medium' : 'text-2xl'}`}>{isOpen ? 'FarmByte.cm' : 'FB.cm'}</p>
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