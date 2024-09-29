import {combineSlices} from "@reduxjs/toolkit";
import uomCategorySlice from "@/pages/inventory/uom-category/uomCategorySlice.ts";
import uomSlice from "@/pages/inventory/uom/uomSlice.ts";
import productCategorySlice from "@/pages/inventory/product-category/productCategorySlice.ts";
import productSlice from "@/pages/inventory/product/productSlice.ts";

export const inventorySlice = combineSlices({
    uomCategory: uomCategorySlice,
    productCategory: productCategorySlice,
    uom: uomSlice,
    product: productSlice,
});
