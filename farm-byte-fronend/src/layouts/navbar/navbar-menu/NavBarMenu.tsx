import {useEffect, useRef, useState} from "react";
import {useTranslation} from "react-i18next";
import {NavbarMenuItemModel} from "@/layouts/model/navbar-menu-item-model.ts";
import NavbarMenuItem from "@/layouts/navbar/navbar-menu/NavmenuMenuItem.tsx";
import {NavbarMenuModel} from "@/layouts/model/navbar-menu-model.ts";

type Props = {
    navbarMenu: NavbarMenuModel
}

const NavBarMenu = ({navbarMenu}: Props) => {

    const {t} = useTranslation(['home']);

    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const closeDropDown = (event) => {
            if(dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpen(false);
            }
        }
        document.addEventListener("click", closeDropDown);
        return () => document.body.removeEventListener("click", closeDropDown);
    }, []);

    const toggleDropDown = () => {
        setIsOpen(!isOpen);
    }



    return (
        <div key={navbarMenu.label} className="relative">
            <div
                ref={dropdownRef}
                onClick={toggleDropDown}
                className="flex flex-row justify-start items-center text-gray-500 hover:text-amber-700 cursor-pointer">
                <span className="font-medium">{t(navbarMenu.label)}</span>
                {
                    navbarMenu.items &&
                    navbarMenu.items.length > 0 &&
                    <span
                        className={`material-symbols-outlined ${isOpen ? 'transition duration-500 ease-out transform rotate-180' : 'transition duration-500 ease-in transform -rotate-180'}`}>
                                        {isOpen ? 'keyboard_arrow_down' : 'keyboard_arrow_up'}
                    </span>
                }
            </div>

                <div className={`absolute top-full -left-0 mt-[1.05rem] w-64 bg-white shadow-2xl z-10 ${isOpen ? 'transition duration-300 ease-in opacity-100 transform scale-110  cursor-pointer' : ' cursor-auto transition duration-300 ease-out opacity-0 scale-75'}`}>
                    {
                        navbarMenu.items && navbarMenu.items.map((navbarMenuItem: NavbarMenuItemModel) => (
                            <NavbarMenuItem key={navbarMenuItem.label + '_' + navbarMenuItem.link} navbarMenuItem={navbarMenuItem} isOpenSubMenu={isOpen} />
                        ))
                    }
                </div>

        </div>
    );
}

export default NavBarMenu;