import {Route, Routes} from "react-router-dom";
import ProductTree from "@/pages/inventory/product/screen/ProductTree.tsx";
import ProductNewForm from "@/pages/inventory/product/screen/ProductNewForm.tsx";
import ProductDetailForm from "@/pages/inventory/product/screen/ProductDetailForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const ProductRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<ProductTree/>}/>
                <Route path="new"  element={<ProductNewForm/>}/>
                <Route path="detail/:productId"  element={<ProductDetailForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};
export default ProductRoute;