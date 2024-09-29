import {useSelector} from "react-redux";
import {useTranslation} from "react-i18next";
import {selectSelectedSidebarMenu} from "@/layouts/state/sidebar-menu-slice.ts";
import {SidebarMenuModel} from "@/layouts/model/sidebarMenuModel.ts";

const NavBarModule = () => {

    const {t} = useTranslation(['home']);

    const selectedSidebarMenu: SidebarMenuModel = useSelector(selectSelectedSidebarMenu);

    return (
        <div className="flex flex-row justify-center items-center pl-5">
            <p className="text-2xl font-medium text-amber-700">{selectedSidebarMenu?.label? t(selectedSidebarMenu?.label): ''}</p>
        </div>
    );
}

export default NavBarModule;