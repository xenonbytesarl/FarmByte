import {ReactNode} from "react";
import Navbar from "@/layouts/Navbar.tsx";
import Sidebar from "@/layouts/Sidebar.tsx";

type Props = {
    children: ReactNode;
}

const Layout = ({ children }: Props) => {
    return (
        <>
            <div className="flex flex-col min-h-screen">
                <Navbar/>
                <div className=" md:hidden container mx-auto flex-1 py-10">
                    {children}
                </div>
                <div className="hidden md:block">
                    <Sidebar/>
                    <div className="container mx-auto flex-1 py-10">
                        {children}
                    </div>
                </div>
            </div>
        </>
    );
}

export default Layout;