import {Navigate, Route, Routes} from "react-router-dom";
import ProductCategoryTree from "@/pages/inventory/product-category/screen/ProductCategoryTree.tsx";
import UomCategoryTree from "@/pages/inventory/uom-category/screen/UomCategoryTree.tsx";
import UomTree from "@/pages/inventory/uom/screen/UomTree.tsx";
import ProductTree from "@/pages/inventory/product/screen/ProductTree.tsx";
import Layout from "@/layouts/Layout.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const InventoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index path="/" element={<Navigate to="/inventory/products"/>}/>
                <Route index path="/products" element={<Layout><ProductTree/></Layout>}/>
                <Route index path="/product-categories" element={<Layout><ProductCategoryTree/></Layout>}/>
                <Route index path="/uom-categories" element={<Layout><UomCategoryTree/></Layout>}/>
                <Route index path="/uoms" element={<Layout><UomTree/></Layout>}/>
                <Route path="*" element={<Layout><NotFound/></Layout>}/>
            </Route>
        </Routes>
    );
}

export default InventoryRoute;