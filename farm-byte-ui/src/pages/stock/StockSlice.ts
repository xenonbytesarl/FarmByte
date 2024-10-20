import {combineSlices} from "@reduxjs/toolkit";
import uomCategorySlice from "@/pages/stock/uom-category/UomCategorySlice.ts";
import uomSlice from "@/pages/stock/uom/UomSlice.ts";
import productCategorySlice from "@/pages/stock/product-category/ProductCategorySlice.ts";
import productSlice from "@/pages/stock/product/ProductSlice.ts";

export const stockSlice = combineSlices({
    uomCategory: uomCategorySlice,
    productCategory: productCategorySlice,
    uom: uomSlice,
    product: productSlice,
});
