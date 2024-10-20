import {Navigate, Route, Routes} from "react-router-dom";
import UomCategoryRoute from "@/pages/inventory/uom-category/UomCategoryRoute.tsx";
import NotFound from "@/pages/not-found/NotFound.tsx";
import UomRoute from "@/pages/inventory/uom/UomRoute.tsx";
import ProductCategoryRoute from "@/pages/inventory/product-category/ProductCategoryRoute.tsx";
import ProductRoute from "@/pages/inventory/product/ProductRoute.tsx";
import StockLocationRoute from "@/pages/inventory/inventory-emplacement/StockLocationRoute.tsx";

const StockRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index path="/" element={<Navigate to="products"/>}/>
                <Route path="products/*" element={<ProductRoute/>}/>
                <Route path="product-categories/*" element={<ProductCategoryRoute/>}/>
                <Route path="uom-categories/*" element={<UomCategoryRoute/>}/>
                <Route path="uoms/*" element={<UomRoute/>}/>
                <Route path="stock-locations/*" element={<StockLocationRoute/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
}

export default StockRoute;