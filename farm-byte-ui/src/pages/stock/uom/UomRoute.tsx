import {Route, Routes} from "react-router-dom";
import UomTree from "@/pages/stock/uom/UomTree.tsx";
import UomForm from "@/pages/stock/uom/UomForm.tsx";
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