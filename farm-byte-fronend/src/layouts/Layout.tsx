import {ReactNode} from "react";
import Sidebar from "@/layouts/Sidebar.tsx";
import WebNav from "@/components/WebNav.tsx";
import MobileNav from "@/components/MobileNav.tsx";

type Props = {
    children: ReactNode;
}

const Layout = ({ children }: Props) => {
    return (
        <>
            <div className="flex flex-row min-h-screen">
                <div className="hidden md:block">
                    <div className="flex flex-row">
                        <Sidebar/>
                        <div className="flex flex-col min-h-screen">
                            <WebNav/>
                            <div className="container mx-auto flex-1 py-10">
                                {children}
                            </div>
                        </div>
                    </div>
                </div>
                <div className="md:hidden">
                    <MobileNav/>
                    <div className="container mx-auto flex-1 py-10">
                        {children}
                    </div>
                </div>
            </div>
        </>
    );
}

export default Layout;