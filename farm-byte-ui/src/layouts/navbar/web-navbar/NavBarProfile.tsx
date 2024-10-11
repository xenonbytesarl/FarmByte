import React, {useEffect, useRef, useState} from "react";
import {useNavigate} from "react-router-dom";
import {getImageUrl} from "@/utils/imageUtils.ts";

const NavBarProfile = () => {

    const dropdownRef = useRef<HTMLDivElement>(null);
    const [isOpenProfileMenu, setIsOpenProfileMenu] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        // eslint-disable-next-line @typescript-eslint/ban-ts-comment
        // @ts-expect-error
        const closeDropDown = (event) => {
            if(dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setIsOpenProfileMenu(false);
            }
        }
        document.addEventListener("click", closeDropDown);
        return () => document.body.removeEventListener("click", closeDropDown);
    }, []);

    const toggleDropDown = () => {
        setIsOpenProfileMenu(!isOpenProfileMenu);
    }

    const handleNavigateTo = (event: React.MouseEvent<HTMLDivElement>, link: string) => {
        //TODO continue to find how to hide sub menu
        event.preventDefault();
        if(isOpenProfileMenu) {
            navigate(link);
        }
    }

    return (
        <div className="relative">
            <div
                ref={dropdownRef}
                onClick={toggleDropDown}
                className="flex flex-row justify-start items-center text-gray-500 hover:text-amber-700 cursor-pointer">
                <span className="font-medium">
                    <img src={getImageUrl('/images/profile.png')} alt="..." className="size-8 object-cover rounded-full"/>
                </span>

            </div>
            <div
                className={`absolute top-full -right-0 mt-[1.65rem] w-32 bg-white shadow-2xl z-10 rounded-b-lg ${isOpenProfileMenu ? 'transition duration-300 ease-in opacity-100 transform scale-110 cursor-pointer' : 'cursor-auto transition duration-300 ease-out opacity-0 scale-75'}`}>
                <div onClick={(event) => handleNavigateTo(event,'/user/profile')}
                      className="block text-gray-500 p-2 text-sm hover:text-white hover:bg-gradient-to-r hover:from-amber-800 hover:to-amber-400 hover:transition-all hover:ease-in-out hover:duration-300 ">My profile</div>
                <div onClick={(event) => handleNavigateTo(event, '/logout')}
                      className="block text-gray-500 p-2 text-sm hover:text-white  rounded-b-lg hover:bg-gradient-to-r hover:from-amber-800 hover:to-amber-400  hover:transition-all hover:ease-in-out hover:duration-300 ">Logout</div>
            </div>
        </div>
    );
}

export default NavBarProfile;