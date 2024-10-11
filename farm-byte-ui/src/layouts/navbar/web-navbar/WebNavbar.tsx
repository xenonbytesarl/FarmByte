import NavbarMenuBar from "@/layouts/navbar/web-navbar/NavbarMenuBar.tsx";
import NavBarLanguage from "@/layouts/navbar/web-navbar/NavBarLanguage.tsx";
import NavBarProfile from "@/layouts/navbar/web-navbar/NavBarProfile.tsx";
import NavBarModule from "@/layouts/navbar/web-navbar/NavBarModule.tsx";
import {useSelector} from "react-redux";
import {selectIsOpenSidebarMenu} from "@/layouts/sidebar/SidebarMenuSlice.ts";

const WebNavbar = () => {
    const isOpen: boolean = useSelector(selectIsOpenSidebarMenu);
    return (
        <div className={`sticky top-0 flex flex-row justify-between items-center py-6 px-8 bg-white shadow-lg z-10 ${isOpen ? 'ml-72 transition-all ease-linear duration-300' : 'ml-24 transition-all ease-linear duration-300'}`}>
            <div className="flex flex-row justify-start items-center">
                <NavBarModule/>
                <NavbarMenuBar/>
            </div>
            <div className="flex flex-row justify-end items-center">
                <NavBarLanguage/>
                <NavBarProfile/>
            </div>
        </div>
    );
}
export default WebNavbar;