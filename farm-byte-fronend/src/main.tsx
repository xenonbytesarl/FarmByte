import {createRoot} from 'react-dom/client'
import {Provider} from "react-redux";
import {BrowserRouter as Router} from "react-router-dom";
import AppRoute from "@/AppRoute.tsx";
import {store} from "@/store/store.ts";
import './global.css'
import './i18n';
import {findProducts} from "@/pages/inventory/product/productSlice.ts";
import {Direction} from "@/constants/directionConstant.ts";

store.dispatch(findProducts({page: 0, size: 5, attribute: "name", direction: Direction.ASC}))
createRoot(document.getElementById('root')!).render(

    <Provider store={store}>
        <Router>
            <AppRoute/>
        </Router>
    </Provider>

)
