import {createAsyncThunk, createEntityAdapter, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {ProductCategoryModel} from "@/pages/inventory/product-category/ProductCategoryModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import productCategoryService from "@/pages/inventory/product-category/ProductCategroyService.ts";


const productCategoryAdapter = createEntityAdapter<ProductCategoryModel>({
});

const productCategoryInitialState = productCategoryAdapter.getInitialState({
    loading: false,
    pageSize: 0,
    totalElements: 0,
    message: '',
    error: null
});


export const findProductCategories = createAsyncThunk('productCategory/findProductCategories', async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<ProductCategoryModel>>> => {

    try {
        return productCategoryService.findProductCategories(findParam);
    } catch (error) {
        console.log('error', error);
    }
});


const productCategorySlice = createSlice({
    name: "productCategory",
    initialState: productCategoryInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findProductCategories.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(findProductCategories.fulfilled, (state, action: PayloadAction<SuccessResponseModel<PageModel<ProductCategoryModel>>>) => {
                state.loading = false;
                state.pageSize = action.payload.data.content.pageSize;
                state.totalElements = action.payload.data.content.totalElements;
                state.message = action.payload.message;
                productCategoryAdapter.upsertMany(state, action.payload.data.content.elements)
            })
            .addCase(findProductCategories.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.productCategory.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.productCategory.totalElements;
export const getMessage = (state: RootState) => state.inventory.productCategory.message;
export const getError = (state: RootState) => state.inventory.productCategory.error;
export const getLoading = (state: RootState) => state.inventory.productCategory.loading;

export const {
    selectAll: selectProductCategories,
    selectIds: selectProductCategoryIds,
    selectById: selectProductCategoryById,
    selectEntities: selectProductCategoryEntities,
} = productCategoryAdapter.getSelectors((state: RootState) => state.inventory.productCategory);

export default productCategorySlice.reducer;