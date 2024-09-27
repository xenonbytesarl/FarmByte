import WebNav from "@/components/WebNav";
import MobileNav from "@/components/MobileNav.tsx";

const Navbar = () => {
    return (
        <>
            <div className="hidden md:block">
                <WebNav/>
            </div>
            <div className="md:hidden">
                <MobileNav/>
            </div>
        </>
    );
}

export default Navbar;