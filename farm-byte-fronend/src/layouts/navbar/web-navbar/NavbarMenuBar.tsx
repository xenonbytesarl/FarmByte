import {useSelector} from "react-redux";
import {NavbarMenuModel} from "@/layouts/model/NavbarMenuModel.ts";
import NavBarMenu from "@/layouts/navbar/web-navbar/navbar-menu/NavBarMenu.tsx";
import {selectNavbarMenus} from "@/layouts/navbar/NavbarMenuSlice.ts";

const NavbarMenuBar = () => {

    const navbarMenus: NavbarMenuModel[] = useSelector(selectNavbarMenus);

    return (
        <div className="flex flex-row justify-start items-center pl-10 gap-6 text-lg">
            {
                navbarMenus?.map((navbarMenu: NavbarMenuModel) => (
                    <NavBarMenu key={navbarMenu.label} navbarMenu={navbarMenu}/>
                ))
            }
        </div>
    );
}

export default NavbarMenuBar;