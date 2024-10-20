import {createAsyncThunk, createEntityAdapter, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {ProductModel} from "@/pages/stock/product/ProductModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import productService from "@/pages/stock/product/ProductService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {UNKNOWN_ERROR} from "@/shared/constant/globalConstant.ts";
import {ErrorResponseModel} from "@/shared/model/errorResponseModel.ts";
import {AxiosError} from "axios";


const productAdapter = createEntityAdapter<ProductModel>({
});

const productInitialState = productAdapter.getInitialState({
    loading: false,
    currentProduct: null as any,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});


export const findProducts = createAsyncThunk('product/findProducts', async (findParam: FindParamModel, {rejectWithValue})=> {
    try {
        const response =  await productService.findProducts(findParam);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const searchProducts = createAsyncThunk('product/searchProducts', async (searchParam: SearchParamModel, {rejectWithValue})=> {
    try {
        const response =  await productService.searchProducts(searchParam);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const findProductById = createAsyncThunk('product/findProductById', async (productId: string, {rejectWithValue})=> {
    try {
        const response =  await productService.findProductById(productId);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const updateProduct = createAsyncThunk('product/updateProduct', async ({productId, product, file}: {productId: string, product: ProductModel, file: File}, {rejectWithValue})=> {
    try {
        const response =  await productService.updateProduct(productId, product, file);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const createProduct = createAsyncThunk('product/createProduct', async ({product, file}: {product: ProductModel, file: File}, {rejectWithValue})=> {
    try {
        const response =  await productService.createProduct(product, file);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if (error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if (error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

const productSlice = createSlice({
    name: "product",
    initialState: productInitialState,
    reducers: {
        resetCurrentProduct: (state) => {
            state.currentProduct = null;
        }
    },
    extraReducers: (builder) => {
        builder
            .addCase(createProduct.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                productAdapter.addOne(state, content);
            })
            .addCase(updateProduct.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.message = message;
                productAdapter.updateOne(state, {id: content.id, changes: content});
            })
            .addCase(findProductById.fulfilled, (state, action) => {
                const {content} = action.payload;
                state.loading = false;
                state.currentProduct = content;
                productAdapter.addOne(state, content);
            })
            .addMatcher(
                isAnyOf(
                    findProducts.fulfilled,
                    searchProducts.fulfilled
                ), (state, action) => {
                    const {content} = action.payload;
                    state.loading = false;
                    state.pageSize = content.pageSize as number;
                    state.totalElements = content.totalElements as number;
                    state.totalPages = content.totalPages as number;
                    productAdapter.setAll(state, content.elements);
                })
            .addMatcher(
                isAnyOf(
                    findProducts.pending,
                    searchProducts.pending,
                    findProductById.pending,
                    updateProduct.pending,
                    createProduct.pending
                ), (state) => {
                    state.loading = true;
                    state.message = '';
                    state.currentProduct = null;
                    state.error = null;
                })
            .addMatcher(
                isAnyOf(
                    findProducts.rejected,
                    searchProducts.rejected,
                    findProductById.rejected,
                    updateProduct.rejected,
                    createProduct.rejected
                ), (state, action) => {
                    state.loading = false;
                    state.message = '';
                    state.currentProduct = null;
                    state.error = action.payload as any;
                })
    }
});

export const getPageSize = (state: RootState) => state.stock.product.pageSize;
export const getTotalElements = (state: RootState) => state.stock.product.totalElements;
export const getTotalPages = (state: RootState) => state.stock.product.totalPages;
export const getMessage = (state: RootState) => state.stock.product.message;
export const getCurrentProduct = (state: RootState) => state.stock.product.currentProduct;
export const getError = (state: RootState) => state.stock.product.error;
export const getLoading = (state: RootState) => state.stock.product.loading;

export const {
    selectAll: selectProducts,
    selectIds: selectProductIds,
    selectById: selectProductById,
    selectEntities: selectProductEntities,
} = productAdapter.getSelectors((state: RootState) => state.stock.product);

export const {resetCurrentProduct} = productSlice.actions;

export default productSlice.reducer;