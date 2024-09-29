import {Navigate, Route, Routes} from "react-router-dom";
import ProductCategoryTree from "@/pages/inventory/product-category/screen/ProductCategoryTree.tsx";
import UomTree from "@/pages/inventory/uom/screen/UomTree.tsx";
import ProductTree from "@/pages/inventory/product/screen/ProductTree.tsx";
import UomCategoryRoute from "@/pages/inventory/uom-category/UomCategoryRoute.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const InventoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index path="/" element={<Navigate to="products"/>}/>
                <Route path="products" element={<ProductTree/>}/>
                <Route path="product-categories" element={<ProductCategoryTree/>}/>
                <Route path="uom-categories/*" element={<UomCategoryRoute/>}/>
                <Route path="uoms" element={<UomTree/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
}

export default InventoryRoute;