import {createAsyncThunk, createEntityAdapter, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {ProductCategoryModel} from "@/pages/inventory/product-category/ProductCategoryModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import productCategoryService from "@/pages/inventory/product-category/ProductCategroyService.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {DEFAULT_PAGE_SIZE_OPTIONS, DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";


const productCategoryAdapter = createEntityAdapter<ProductCategoryModel>({
});

const productCategoryInitialState = productCategoryAdapter.getInitialState({
    loading: false,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});


export const findProductCategories = createAsyncThunk('productCategory/findProductCategories', async (findParam: FindParamModel) => {

    try {
        const response = await productCategoryService.findProductCategories(findParam);
        return response.data;
    } catch (error) {
        console.log('error', error);
    }
});

export const searchProductCategories = createAsyncThunk('productCategory/searchProductCategories', async (searchParam: SearchParamModel) => {

    try {
        const response = await productCategoryService.searchProductCategories(searchParam);
        return response.data;
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
                state.pageSize = DEFAULT_SIZE_VALUE;
            })
            .addCase(findProductCategories.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<ProductCategoryModel>>;
                state.loading = false;
                state.totalElements = data.content.totalElements;
                state.message = message;
                productCategoryAdapter.setAll(state, data.content.elements)
            })
            .addCase(findProductCategories.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(searchProductCategories.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(searchProductCategories.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<ProductCategoryModel>>;
                state.loading = false;
                state.pageSize = data.content.pageSize;
                state.totalElements = data.content.totalElements;
                state.message = message;
                productCategoryAdapter.setAll(state, data.content.elements)
            })
            .addCase(searchProductCategories.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.productCategory.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.productCategory.totalElements;
export const getTotalPages = (state: RootState) => state.inventory.productCategory.totalPages;
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