import NavbarMenuBar from "@/layouts/navbar/navbar-menu/NavbarMenuBar.tsx";
import {useDispatch, useSelector} from "react-redux";
import {RootDispatch, RootState} from "@/store/store.ts";
import React, {useEffect, useRef, useState} from "react";
import {changeLanguage} from "@/layouts/state/navbar-menu-slice.ts";
import {LanguageModel} from "@/layouts/model/language.ts";

const WebNavbar = () => {

    const selectedSidebarMenu = useSelector((state: RootState) => state.sidebar.selectedSidebarMenu);
    const language = useSelector((state: RootState) => state.navbar.language);
    const [isOpenLanguageMenu, setIsOpenLanguageMenu] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);
    const dispatch = useDispatch<RootDispatch>();

    useEffect(() => {
        const closeDropDown = (event) => {
            if(dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpenLanguageMenu(false);
            }
        }
        document.addEventListener("click", closeDropDown);
        return () => document.body.removeEventListener("click", closeDropDown);
    }, []);

    const toggleDropDown = () => {
        setIsOpenLanguageMenu(!isOpenLanguageMenu);
    }

    const handleChangeLanguage = (event: React.MouseEvent<HTMLDivElement>, language: LanguageModel) => {
        event.preventDefault();
        dispatch(changeLanguage(language));
    }

    return (
        <div className="flex flex-row justify-between items-center py-3 px-8 w-full shadow-lg">
            <div className="flex flex-row justify-start items-center">
                <div className="flex flex-row justify-center items-center pl-5">
                    <p className="text-2xl font-medium text-amber-700">{selectedSidebarMenu?.label}</p>
                </div>
                <NavbarMenuBar/>
            </div>
            <div className="flex flex-row justify-end items-center">
                <div className="relative mr-5 cursor-pointer">
                    <div
                        ref={dropdownRef}
                        onClick={toggleDropDown}
                        className="flex flex-row justify-start items-center text-gray-500 hover:text-amber-700">
                        <span className="font-medium">
                            <img src={`/images/${language.name === 'fr'? 'french': 'english'}.png`} alt="..." className="size-6 object-center rounded-full" />
                        </span>
                    </div>
                    <div
                        className={`absolute top-full -right-0 mt-3.5 w-32 bg-white shadow-2xl z-10 ${isOpenLanguageMenu ? 'transition duration-300 ease-in opacity-100 transform scale-110' : 'transition duration-300 ease-out opacity-0 scale-75'}`}>
                        <div onClick={(event) => handleChangeLanguage(event, {name: 'en'})}
                              className="flex flex-row justify-start items-center gap-2 text-gray-500 p-2 text-sm hover:text-white hover:bg-amber-700 hover:transition-all hover:ease-in-out hover:duration-300 ">
                            <img src="/images/english.png" alt="..." className="size-6 object-center rounded-full" />
                            <span>English</span>
                        </div>
                        <div onClick={(event) => handleChangeLanguage(event, {name: 'fr'})}
                              className="flex flex-row justify-start items-center gap-2 text-gray-500 p-2 text-sm hover:text-white hover:bg-amber-700 hover:transition-all hover:ease-in-out hover:duration-300 ">
                            <img src="/images/french.png" alt="..." className="size-6 object-center rounded-full"/>
                            French
                        </div>
                    </div>
                </div>
                <div className="">Profile</div>
            </div>
        </div>
    );
}
export default WebNavbar;