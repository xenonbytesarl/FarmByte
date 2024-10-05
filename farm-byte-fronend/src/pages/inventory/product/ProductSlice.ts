import {createAsyncThunk, createEntityAdapter, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {ProductModel} from "@/pages/inventory/product/ProductModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import productService from "@/pages/inventory/product/ProductService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";


const productAdapter = createEntityAdapter<ProductModel>({
});

const productInitialState = productAdapter.getInitialState({
    loading: false,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});


export const findProducts = createAsyncThunk('product/findProducts', async (findParam: FindParamModel)=> {
    try {
        const response =  await productService.findProducts(findParam);
        return response.data;
    } catch (error) {
        console.log('error', error);
    }
});

export const searchProducts = createAsyncThunk('product/searchProducts', async (searchParam: SearchParamModel)=> {
    try {
        const response =  await productService.searchProducts(searchParam);
        return response.data;
    } catch (error) {
        console.log('error', error);
    }
});


const productSlice = createSlice({
    name: "product",
    initialState: productInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findProducts.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(findProducts.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<ProductModel>>;
                state.loading = false;
                state.totalElements = data.content.totalElements;
                state.totalPages = data.content.totalPages;
                state.message = message;
                productAdapter.setAll(state, data.content.elements)
            })
            .addCase(findProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(searchProducts.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(searchProducts.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<ProductModel>>;
                state.loading = false;
                state.pageSize = data.content.pageSize;
                state.totalElements = data.content.totalElements;
                state.totalPages = data.content.totalPages;
                state.message = message;
                productAdapter.setAll(state, data.content.elements)
            })
            .addCase(searchProducts.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.product.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.product.totalElements;
export const getTotalPages = (state: RootState) => state.inventory.product.totalPages;
export const getMessage = (state: RootState) => state.inventory.product.message;
export const getError = (state: RootState) => state.inventory.product.error;
export const getLoading = (state: RootState) => state.inventory.product.loading;

export const {
    selectAll: selectProducts,
    selectIds: selectProductIds,
    selectById: selectProductById,
    selectEntities: selectProductEntities,
} = productAdapter.getSelectors((state: RootState) => state.inventory.product);

export default productSlice.reducer;