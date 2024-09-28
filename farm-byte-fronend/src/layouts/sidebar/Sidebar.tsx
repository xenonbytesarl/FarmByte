import {useState} from "react";
import {SidebarMenuModel} from "@/layouts/model/sidebar-menu.ts";
import {sidebarMenus} from "@/layouts/data/sidebar-menu-data.ts";
import {Link} from "react-router-dom";

const Sidebar = () => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleSidebar =  () => {
        setIsOpen(!isOpen);
    }
    return (
        <div className={`flex flex-col bg-amber-800 min-h-screen py-3 text-white ${isOpen? 'w-80 transition-all ease-linear duration-300': 'w-20 transition-all ease-linear duration-300'}`}>
            <div className="relative flex flex-row justify-between items-center px-5 mb-5 w-full">
                <div className={`flex flex-row justify-between items-center w-[95%]`}>
                    <p className={` ${isOpen ? 'block text-xl font-medium' : 'hidden text-2xl text-center'}`}>FarmByte.cm</p>
                </div>
                <p onClick={toggleSidebar}
                   className={`absolute -right-3.5 material-symbols-outlined text-white cursor-pointer p-1 bg-amber-600 rounded-full ${isOpen ? '' : 'mt-8'}`}>{isOpen ? 'arrow_back' : 'arrow_forward'}</p>
            </div>
            <div className="flex flex-col justify-start items-start text-lg">
                {
                    sidebarMenus.map((sidebarMenu : SidebarMenuModel) => (
                        <Link to={sidebarMenu.link}
                            key={sidebarMenu.label}
                            className={`flex flex-row ${isOpen? 'justify-start': 'justify-center'} items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out`}>
                            <span className="material-symbols-outlined">{sidebarMenu.icon}</span>
                            <p className={`${isOpen? 'transition-all ease-out duration-300 opacity-100': 'hidden transition-all ease-in duration-300 opacity-0'}`}>{sidebarMenu.label}</p>
                        </Link>
                    ))
                }
            </div>
            <div className={`flex flex-row justify-center items-end mt-auto mx-auto border-t-2 py-5 w-[95%] border-amber-700 ${isOpen? 'block': 'hidden'}`}>
                <span>copyright    &#169;2024</span>
            </div>
        </div>
    );
}

export default Sidebar;