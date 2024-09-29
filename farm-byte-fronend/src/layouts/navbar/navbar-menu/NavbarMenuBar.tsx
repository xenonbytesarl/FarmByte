import {useSelector} from "react-redux";
import {NavbarMenuModel} from "@/layouts/model/navbarMenuModel.ts";
import NavBarMenu from "@/layouts/navbar/navbar-menu/NavBarMenu.tsx";
import {selectNavbarMenus} from "@/layouts/state/navbar-menu-slice.ts";

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