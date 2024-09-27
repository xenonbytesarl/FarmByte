import {Link} from "react-router-dom";
import {NavbarMenuItemModel} from "@/layouts/model/navbar-menu-item-model.ts";

type Props = {
    navbarMenuItem: NavbarMenuItemModel
}

const NavbarMenuItem = ({navbarMenuItem}: Props) => {
    return (
        <Link to={navbarMenuItem.link} className="block text-gray-500 p-2 text-sm hover:text-white hover:bg-amber-700 hover:transition-all hover:ease-in-out hover:duration-300 ">{navbarMenuItem.label}</Link>
    );
}

export default NavbarMenuItem;