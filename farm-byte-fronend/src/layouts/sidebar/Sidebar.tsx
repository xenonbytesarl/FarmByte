

const Sidebar = () => {
    return (
        <>
            <div className="flex flex-col bg-amber-800 min-h-screen py-3 w-72 text-white">
                <div className="flex flex-row justify-between items-center px-3 mb-5 w-full">
                    <div className=" flex flex-row justify-between items-center w-[95%]">
                        <p className="text-2xl font-medium">FarmByte.cm</p>
                        <span className="material-symbols-outlined cursor-pointer rounded-full">menu</span>
                    </div>
                </div>
                <div className="flex flex-col justify-start items-start text-xl">
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">dashboard</span>
                        <p>Dashboard</p>
                    </div>
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">inventory</span>
                        <p>Inventory</p>
                    </div>
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">storefront</span>
                        <p>Sale</p>
                    </div>
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">shopping_bag</span>
                        <p>Purchase</p>
                    </div>
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">receipt</span>
                        <p>Invoice</p>
                    </div>
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">agriculture</span>
                        <p>Farm</p>
                    </div>
                    <div
                        className="flex flex-row justify-start items-center gap-3 px-3 py-5 cursor-pointer w-full text-white hover:bg-amber-900/40 hover:text-amber-500 hover:transition hover:duration-300 hover:ease-in-out">
                        <span className="material-symbols-outlined">settings</span>
                        <p>Setting</p>
                    </div>
                </div>
                <div className="flex flex-row justify-center items-end">
                    <span>copyright	&#169;2024</span>
                </div>
            </div>
        </>
    );
}

export default Sidebar;