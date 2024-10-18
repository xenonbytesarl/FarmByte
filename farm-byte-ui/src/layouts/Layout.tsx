import Sidebar from "@/layouts/sidebar/Sidebar.tsx";
import WebNavbar from "@/layouts/navbar/web-navbar/WebNavbar.tsx";
import MobileNavbar from "@/layouts/navbar/mobile-navbar/MobileNavbar.tsx";
import {Outlet} from "react-router-dom";
import {useSelector} from "react-redux";
import {selectIsOpenSidebarMenu} from "@/layouts/sidebar/SidebarMenuSlice.ts";



const Layout = () => {
    const isOpen: boolean = useSelector(selectIsOpenSidebarMenu);
    return (
        <div className="flex flex-row min-h-screen overflow-x-hidden">
            <div className="hidden md:block">
                <div className="flex flex-row w-screen">
                    <div className="hidden md:block">
                        <Sidebar/>
                    </div>
                    <div className="flex flex-col w-full">
                        <div className="hidden md:block fixed w-full z-10">
                            <WebNavbar/>
                        </div>
                        <div className="md:hidden">
                            <MobileNavbar/>
                        </div>
                        <div
                            className={`min-h-full flex-1 mt-32 ${isOpen ? 'ml-72 transition-all ease-linear duration-300' : 'ml-24 transition-all ease-linear duration-300'}`}>
                            <Outlet/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Layout;