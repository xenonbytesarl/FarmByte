import {ReactNode} from "react";
import Sidebar from "@/layouts/sidebar/Sidebar.tsx";
import WebNavbar from "@/layouts/navbar/WebNavbar.tsx";
import MobileNavbar from "@/layouts/navbar/MobileNavbar.tsx";

type Props = {
    children: ReactNode;
}

const Layout = ({ children }: Props) => {
    return (
        <div className="flex flex-row min-h-screen">
            <div className="hidden md:block">
                <div className="flex flex-row w-screen">
                    <Sidebar/>
                    <div className="flex flex-col w-full">
                        <WebNavbar/>
                        <div className="container mx-auto flex-1 py-10">
                            {children}
                        </div>
                    </div>
                </div>
            </div>
            <div className="md:hidden">
                <MobileNavbar/>
                <div className="container mx-auto flex-1 py-10">
                    {children}
                </div>
            </div>
        </div>
    );
}

export default Layout;