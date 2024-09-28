import {inventoryNavbarMenus} from "@/layouts/data/inventory-menu.ts";
import {NavbarMenuModel} from "@/layouts/model/navbar-menu-model.ts";
import NavBarMenu from "@/layouts/navbar/navbar-menu/NavBarMenu.tsx";

const NavbarMenuBar = () => {

    return (
        <div className="flex flex-row justify-start items-center pl-10 gap-6 text-lg">
            {
                inventoryNavbarMenus.map((navbarMenu: NavbarMenuModel) => (
                    <NavBarMenu key={navbarMenu.label} navbarMenu={navbarMenu}/>
                ))
            }
        </div>
    );
}

export default NavbarMenuBar;