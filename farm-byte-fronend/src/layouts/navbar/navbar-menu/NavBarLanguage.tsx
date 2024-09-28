import React, {useEffect, useRef, useState} from "react";
import {LanguageModel} from "@/layouts/model/language.ts";
import {useDispatch, useSelector} from "react-redux";
import {useTranslation} from "react-i18next";
import {changeLanguage} from "@/layouts/state/navbar-menu-slice.ts";
import {RootDispatch, RootState} from "@/store/store.ts";

const NavBarLanguage = () => {

    const {i18n} = useTranslation(['home']);
    const dispatch = useDispatch<RootDispatch>();
    const language = useSelector((state: RootState) => state.navbar.language);

    const dropdownRef = useRef<HTMLDivElement>(null);
    const [isOpenLanguageMenu, setIsOpenLanguageMenu] = useState(false);

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
        //TODO continue to find how to hide sub menu
        event.preventDefault();
       if(isOpenLanguageMenu) {
           i18n.changeLanguage(language.name).then();
           dispatch(changeLanguage(language));
       }
    }

    return (
        <div className="relative mr-5">
            <div
                ref={dropdownRef}
                onClick={toggleDropDown}
                className="flex flex-row justify-start items-center text-gray-500 hover:text-amber-700 cursor-pointer">
                    <span className="font-medium">
                        <img src={`/images/${language.name === 'fr' ? 'french' : 'english'}.png`} alt="..."
                             className="size-6 object-cover rounded-full"/>
                    </span>
            </div>
            <div
                className={`absolute top-full -right-0 mt-3.5 w-32 bg-white shadow-2xl z-10 ${isOpenLanguageMenu ? 'transition-all duration-300 ease-in opacity-100 transform scale-110 cursor-pointer' : 'cursor-auto transition-all duration-300 ease-out opacity-0 scale-75'}`}>
                <div onClick={(event) => handleChangeLanguage(event, {name: 'en'})}
                     className="flex flex-row justify-start items-center gap-2 text-gray-500 p-2 text-sm hover:text-white hover:bg-amber-700 hover:transition-all hover:ease-in-out hover:duration-300 ">
                    <img src="/images/english.png" alt="..." className="size-6 object-cover rounded-full"/>
                    <span>English</span>
                </div>
                <div onClick={(event) => handleChangeLanguage(event, {name: 'fr'})}
                     className="flex flex-row justify-start items-center gap-2 text-gray-500 p-2 text-sm hover:text-white hover:bg-amber-700 hover:transition-all hover:ease-in-out hover:duration-300 ">
                    <img src="/images/french.png" alt="..." className="size-6 object-cover rounded-full"/>
                    French
                </div>
            </div>
        </div>
    );
}

export default NavBarLanguage;