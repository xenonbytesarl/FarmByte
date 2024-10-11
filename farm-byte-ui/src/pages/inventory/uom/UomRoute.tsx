import {Route, Routes} from "react-router-dom";
import UomTree from "@/pages/inventory/uom/UomTree.tsx";
import UomForm from "@/pages/inventory/uom/UomForm.tsx";
import NotFound from "@/pages/not-found/NotFound.tsx";

const UomRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<UomTree/>}/>
                <Route path="new"  element={<UomForm/>}/>
                <Route path="details/:uomId"  element={<UomForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};

export default UomRoute;