import {combineSlices} from "@reduxjs/toolkit";
import uomCategorySlice from "@/pages/inventory/uom-category/UomCategorySlice.ts";
import uomSlice from "@/pages/inventory/uom/UomSlice.ts";
import productCategorySlice from "@/pages/inventory/product-category/ProductCategorySlice.ts";
import productSlice from "@/pages/inventory/product/ProductSlice.ts";

export const inventorySlice = combineSlices({
    uomCategory: uomCategorySlice,
    productCategory: productCategorySlice,
    uom: uomSlice,
    product: productSlice,
});
