import React from "react";
import {Navigate, Route, Routes} from "react-router-dom";
import UomCategoryRoute from "@/pages/inventory/uom-category/UomCategoryRoute.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";
import UomRoute from "@/pages/inventory/uom/UomRoute.tsx";
import ProductCategoryRoute from "@/pages/inventory/product-category/ProductCategoryRoute.tsx";
import ProductRoute from "@/pages/inventory/product/ProductRoute.tsx";

const InventoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index path="/" element={<Navigate to="products"/>}/>
                <Route path="products/*" element={<ProductRoute/>}/>
                <Route path="product-categories/*" element={<ProductCategoryRoute/>}/>
                <Route path="uom-categories/*" element={<UomCategoryRoute/>}/>
                <Route path="uoms/*" element={<UomRoute/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
}

export default InventoryRoute;