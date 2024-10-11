import {createAsyncThunk, createEntityAdapter, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {ProductModel} from "@/pages/inventory/product/ProductModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import productService from "@/pages/inventory/product/ProductService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {UNKNOWN_ERROR} from "@/shared/constant/globalConstant.ts";



const productAdapter = createEntityAdapter<ProductModel>({
});

const productInitialState = productAdapter.getInitialState({
    loading: false,
    currentProduct: null,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});


export const findProducts = createAsyncThunk('product/findProducts', async (findParam: FindParamModel, {rejectWithValue})=> {
    try {
        const response =  await productService.findProducts(findParam);
        return response.data;
    } catch (error) {
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
        return response.data;
    } catch (error) {
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
        return response.data;
    } catch (error) {
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
        return response.data;
    } catch (error) {
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
        return response.data;
    } catch (error) {
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
                const {data, message} = action.payload as SuccessResponseModel<ProductModel>;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                productAdapter.addOne(state, data.content);
            })
            .addCase(updateProduct.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<ProductModel>;
                state.loading = false;
                state.message = message;
                productAdapter.updateOne(state, {id: data.content.id, changes: data.content});
            })
            .addCase(findProductById.fulfilled, (state, action) => {
                const {data} = action.payload as SuccessResponseModel<ProductModel>;
                state.loading = false;
                state.currentProduct = data.content;
                productAdapter.addOne(state, data.content);
            })
            .addMatcher(
                isAnyOf(
                    findProducts.fulfilled,
                    searchProducts.fulfilled
                ), (state, action) => {
                    const {data} = action.payload as SuccessResponseModel<PageModel<ProductModel>>;
                    state.loading = false;
                    state.totalElements = data.content.totalElements;
                    state.totalPages = data.content.totalPages;
                    productAdapter.setAll(state, data.content.elements);
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
                    state.error = action.payload;
                })
    }
});

export const getPageSize = (state: RootState) => state.inventory.product.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.product.totalElements;
export const getTotalPages = (state: RootState) => state.inventory.product.totalPages;
export const getMessage = (state: RootState) => state.inventory.product.message;
export const getCurrentProduct = (state: RootState) => state.inventory.product.currentProduct;
export const getError = (state: RootState) => state.inventory.product.error;
export const getLoading = (state: RootState) => state.inventory.product.loading;

export const {
    selectAll: selectProducts,
    selectIds: selectProductIds,
    selectById: selectProductById,
    selectEntities: selectProductEntities,
} = productAdapter.getSelectors((state: RootState) => state.inventory.product);

export const {resetCurrentProduct} = productSlice.actions;

export default productSlice.reducer;