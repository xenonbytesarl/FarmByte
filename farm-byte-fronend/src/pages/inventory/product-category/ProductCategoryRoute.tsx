import {Route, Routes} from "react-router-dom";
import ProductCategoryTree from "@/pages/inventory/product-category/screen/ProductCategoryTree.tsx";
import ProductCategoryNewForm from "@/pages/inventory/product-category/screen/ProductCategoryNewForm.tsx";
import ProductCategoryDetailForm from "@/pages/inventory/product-category/screen/ProductCategoryDetailForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const ProductCategoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<ProductCategoryTree/>}/>
                <Route path="new"  element={<ProductCategoryNewForm/>}/>
                <Route path="detail/:productCategoryId"  element={<ProductCategoryDetailForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};
export default ProductCategoryRoute;