import NavbarMenuBar from "@/layouts/navbar/navbar-menu/NavbarMenuBar.tsx";

const WebNavbar = () => {
    return (
        <div className="flex flex-row justify-between items-center p-3 w-full shadow-lg">
            <div className="flex flex-row justify-start items-center">
                <div className="flex flex-row justify-center items-center pl-5">
                    <p className="text-2xl font-medium text-amber-700">Inventory</p>
                </div>
                <NavbarMenuBar/>
            </div>
            <div className="flex flex-row justify-end items-center">
                <div className="">Language</div>
                <div className="">Profile</div>
            </div>
        </div>
    );
}
export default WebNavbar;