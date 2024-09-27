import WebNavbar from "@/layouts/navbar/WebNavbar.tsx";
import MobileNavbar from "@/layouts/navbar/MobileNavbar.tsx";

const Navbar = () => {
    return (
        <>
            <div className="hidden md:block">
                <WebNavbar/>
            </div>
            <div className="md:hidden">
                <MobileNavbar/>
            </div>
        </>
    );
}

export default Navbar;