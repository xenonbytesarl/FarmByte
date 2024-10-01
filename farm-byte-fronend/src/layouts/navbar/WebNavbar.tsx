import NavbarMenuBar from "@/layouts/navbar/navbar-menu/NavbarMenuBar.tsx";
import NavBarLanguage from "@/layouts/navbar/navbar-menu/NavBarLanguage.tsx";
import NavBarProfile from "@/layouts/navbar/navbar-menu/NavBarProfile.tsx";
import NavBarModule from "@/layouts/navbar/navbar-menu/NavBarModule.tsx";

const WebNavbar = () => {

    return (
        <div className="flex flex-row justify-between items-center py-6 px-8 w-full bg-white shadow-lg">
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