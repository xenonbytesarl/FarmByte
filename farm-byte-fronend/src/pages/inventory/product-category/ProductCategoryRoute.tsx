import {Route, Routes} from "react-router-dom";
import ProductCategoryTree from "@/pages/inventory/product-category/ProductCategoryTree.tsx";
import ProductCategoryForm from "@/pages/inventory/product-category/ProductCategoryForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const ProductCategoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<ProductCategoryTree/>}/>
                <Route path="new"  element={<ProductCategoryForm/>}/>
                <Route path="details/:productCategoryId"  element={<ProductCategoryForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};
export default ProductCategoryRoute;