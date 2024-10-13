import {createAsyncThunk, createEntityAdapter, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {ProductCategoryModel} from "@/pages/inventory/product-category/ProductCategoryModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import productCategoryService from "@/pages/inventory/product-category/ProductCategroyService.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {UNKNOWN_ERROR} from "@/shared/constant/globalConstant.ts";
import {ErrorResponseModel} from "@/shared/model/errorResponseModel.ts";
import {AxiosError} from "axios";


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


export const findProductCategories = createAsyncThunk('productCategory/findProductCategories', async (findParam: FindParamModel, {rejectWithValue}) => {

    try {
        const response =  await productCategoryService.findProductCategories(findParam);
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

export const searchProductCategories = createAsyncThunk('productCategory/searchProductCategories', async (searchParam: SearchParamModel, {rejectWithValue}) => {

    try {
        const response =  await productCategoryService.searchProductCategories(searchParam);
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


export const findProductCategoryById = createAsyncThunk('productCategory/findProductCategoryById', async (productCategoryId: string, {rejectWithValue}) => {

    try {
        const response =  await productCategoryService.findProductCategoryById(productCategoryId);
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

export const updateProductCategory = createAsyncThunk('productCategory/updateProductCategory', async ({productCategoryId, productCategory}:{
                                                                                                          productCategoryId: string,
                                                                                                          productCategory: ProductCategoryModel
                                                                                                      }, {rejectWithValue}) => {
    try {
        const response =  await productCategoryService.updateProductCategory(productCategoryId, productCategory);
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

export const createProductCategory = createAsyncThunk('productCategory/createProductCategory', async (productCategory: ProductCategoryModel, {rejectWithValue}) => {
    try {
        const response =  await productCategoryService.createProductCategory(productCategory);
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

const productCategorySlice = createSlice({
    name: "productCategory",
    initialState: productCategoryInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findProductCategoryById.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                productCategoryAdapter.addOne(state, content);
            })
            .addCase(updateProductCategory.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.message = message;
                productCategoryAdapter.updateOne(state, {id: content.id, changes: content});
            })
            .addCase(createProductCategory.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                productCategoryAdapter.addOne(state, content);
            })
            .addMatcher(
                isAnyOf(
                    findProductCategories.fulfilled,
                    searchProductCategories.fulfilled
                ), (state, action) => {
                    const {content, message} = action.payload;
                    state.loading = false;
                    state.pageSize = content.pageSize as number;
                    state.totalElements = content.totalElements as number;
                    state.totalPages = content.totalPages as number;
                    state.message = message;
                    productCategoryAdapter.setAll(state, content.elements);
                })
            .addMatcher(
                isAnyOf(
                    findProductCategories.pending,
                    searchProductCategories.pending,
                    findProductCategoryById.pending,
                    updateProductCategory.pending,
                    createProductCategory.pending
                ), (state) => {
                    state.loading = true;
                    state.message = '';
                    state.error = null;
                })
            .addMatcher(
                isAnyOf(
                    findProductCategories.rejected,
                    searchProductCategories.rejected,
                    findProductCategoryById.rejected,
                    updateProductCategory.rejected,
                    createProductCategory.rejected
                ), (state, action) => {
                    state.loading = false;
                    state.message = '';
                    state.error = action.payload as any;
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