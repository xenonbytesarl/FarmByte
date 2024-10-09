import {Route, Routes} from "react-router-dom";
import UomCategoryTree from "@/pages/inventory/uom-category/UomCategoryTree.tsx";
import UomCategoryForm from "@/pages/inventory/uom-category/UomCategoryForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const UomCategoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<UomCategoryTree/>}/>
                <Route path="new"  element={<UomCategoryForm/>}/>
                <Route path="details/:uomCategoryId"  element={<UomCategoryForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};

export default UomCategoryRoute;