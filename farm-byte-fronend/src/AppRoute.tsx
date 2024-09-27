import {Navigate, Route, Routes} from "react-router-dom";
import Layout from "@/layouts/Layout.tsx";

const AppRoute = () => {
    return (
        <Routes>
            <Route path="/" element={<Layout>HOME PAGE</Layout>} />
            <Route path="/user-profile" element={<span>USER PROFILE</span>} />
            <Route path="*" element={<Navigate to="/"/>} />
        </Routes>
    )
}

export default AppRoute;