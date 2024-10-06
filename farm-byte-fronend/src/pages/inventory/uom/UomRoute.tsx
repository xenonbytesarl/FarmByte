import {Route, Routes} from "react-router-dom";
import UomTree from "@/pages/inventory/uom/screen/UomTree.tsx";
import UomForm from "@/pages/inventory/uom/screen/UomForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const UomRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<UomTree/>}/>
                <Route path="new"  element={<UomForm/>}/>
                <Route path="detail/:uomId"  element={<UomForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};

export default UomRoute;