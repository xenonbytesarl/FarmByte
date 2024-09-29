import {Route, Routes} from "react-router-dom";
import UomCategoryTree from "@/pages/inventory/uom-category/screen/UomCategoryTree.tsx";
import UomCategoryNewForm from "@/pages/inventory/uom-category/screen/UomCategoryNewForm.tsx";
import UomCategoryDetailForm from "@/pages/inventory/uom-category/screen/UomCategoryDetailForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const UomCategoryRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<UomCategoryTree/>}/>
                <Route path="new"  element={<UomCategoryNewForm/>}/>
                <Route path="detail/:uomCategoryId"  element={<UomCategoryDetailForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};

export default UomCategoryRoute;