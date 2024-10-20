import {Route, Routes} from "react-router-dom";
import StockLocationTree from "@/pages/inventory/inventory-emplacement/StockLocationTree.tsx";
import StockLocationForm from "@/pages/inventory/inventory-emplacement/StockLocationForm.tsx";
import NotFound from "@/pages/not-found/NotFound.tsx";


const StockLocationRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<StockLocationTree/>}/>
                <Route path="new"  element={<StockLocationForm/>}/>
                <Route path="details/:stockLocationId"  element={<StockLocationForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};

export default StockLocationRoute;