import {Route, Routes} from "react-router-dom";
import UomTree from "@/pages/inventory/uom/screen/UomTree.tsx";
import UomNewForm from "@/pages/inventory/uom/screen/UomNewForm.tsx";
import UomDetailForm from "@/pages/inventory/uom/screen/UomDetailForm.tsx";
import NotFound from "@/pages/not-found/screen/NotFound.tsx";

const UomRoute = () => {
    return (
        <Routes>
            <Route>
                <Route index  element={<UomTree/>}/>
                <Route path="new"  element={<UomNewForm/>}/>
                <Route path="detail/:uomId"  element={<UomDetailForm/>}/>
                <Route path="*" element={<NotFound/>}/>
            </Route>
        </Routes>
    );
};

export default UomRoute;