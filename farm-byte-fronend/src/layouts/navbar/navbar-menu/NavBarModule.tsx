import {useSelector} from "react-redux";
import {useTranslation} from "react-i18next";
import {RootState} from "@/store/store.ts";

const NavBarModule = () => {
    const {t} = useTranslation(['home']);
    const selectedSidebarMenu = useSelector((state: RootState) => state.sidebar.selectedSidebarMenu);

    return (
        <div className="flex flex-row justify-center items-center pl-5">
            <p className="text-2xl font-medium text-amber-700">{selectedSidebarMenu?.label? t(selectedSidebarMenu?.label): ''}</p>
        </div>
    );
}

export default NavBarModule;