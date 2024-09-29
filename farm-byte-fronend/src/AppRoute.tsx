import {Navigate, Route, Routes} from "react-router-dom";
import InventoryRoute from "@/pages/inventory/InventoryRoute.tsx";
import Layout from "@/layouts/Layout.tsx";
import Dashboard from "@/pages/dashboard/screen/Dashboard.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const AppRoute = () => {
    return (
        <Routes>
            <Route path="/" element={<Layout/>}>
                <Route index path="/" element={<Navigate to="/dashboard"/>}/>
                <Route path="dashboard" element={<Dashboard/>}/>
                <Route path="inventory/*" element={<InventoryRoute/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    )
}

export default AppRoute;