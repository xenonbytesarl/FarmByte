import {useSelector} from "react-redux";
import {NavbarMenuModel} from "@/layouts/model/navbar-menu-model.ts";
import NavBarMenu from "@/layouts/navbar/navbar-menu/NavBarMenu.tsx";
import {RootState} from "@/store/store.ts";

const NavbarMenuBar = () => {

    const selectedSidebarMenu = useSelector((state: RootState) => state.sidebar.selectedSidebarMenu);

    return (
        <div className="flex flex-row justify-start items-center pl-10 gap-6 text-lg">
            {
                selectedSidebarMenu?.navbarMenu?.map((navbarMenu: NavbarMenuModel) => (
                    <NavBarMenu key={navbarMenu.label} navbarMenu={navbarMenu}/>
                ))
            }
        </div>
    );
}

export default NavbarMenuBar;