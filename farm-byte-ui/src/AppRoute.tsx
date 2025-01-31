import {Navigate, Route, Routes} from "react-router-dom";
import Layout from "@/layouts/Layout.tsx";
import StockRoute from "@/pages/stock/StockRoute.tsx";
import Dashboard from "@/pages/dashboard/Dashboard.tsx";
import NotFound from "@/pages/not-found/NotFound.tsx";

const AppRoute = () => {
    return (
        <Routes>
            <Route path="/" element={<Layout/>}>
                <Route index path="/" element={<Navigate to="/dashboard"/>}/>
                <Route path="dashboard" element={<Dashboard/>}/>
                <Route path="stock/*" element={<StockRoute/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    )
}

export default AppRoute;