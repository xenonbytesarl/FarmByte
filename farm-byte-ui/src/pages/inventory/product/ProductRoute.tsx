import {Route, Routes} from "react-router-dom";
import ProductTree from "@/pages/inventory/product/ProductTree.tsx";
import ProductForm from "@/pages/inventory/product/ProductForm.tsx";
import NotFound from "@/pages/not-found/NotFound.tsx";

const ProductRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<ProductTree/>}/>
                <Route path="new"  element={<ProductForm/>}/>
                <Route path="details/:productId"  element={<ProductForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};
export default ProductRoute;