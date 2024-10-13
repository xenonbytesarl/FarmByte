import {createAsyncThunk, createEntityAdapter, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import uomCategoryService from "@/pages/inventory/uom-category/UomCategoryService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";
import {UNKNOWN_ERROR} from "@/shared/constant/globalConstant.ts";
import {ErrorResponseModel} from "@/shared/model/errorResponseModel.ts";
import {AxiosError} from "axios";


const uomCategoryAdapter = createEntityAdapter<UomCategoryModel>({
});

const uomCategoryInitialState = uomCategoryAdapter.getInitialState({
    loading: false,
    pageSize: DEFAULT_SIZE_VALUE,
    totalElements: 0,
    totalPages: 0,
    message: '',
    error: null
});


export const findUomCategories = createAsyncThunk(
    'uomCategory/findUomCategories', async (findParam: FindParamModel, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.findUomCategories(findParam);
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

export const searchUomCategories = createAsyncThunk(
    'uomCategory/searchUomCategories', async (searchParam: SearchParamModel, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.searchUomCategories(searchParam);
            // @ts-ignore
            return {content: response.data?.data.content, message: response.data.message}
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

export const findUomCategoryById = createAsyncThunk(
    'uomCategory/findUomCategoryById', async (uomCategoryId: string, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.findUomCategoryById(uomCategoryId);
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

export const updateUomCategory = createAsyncThunk(
    'uomCategory/updateUomCategory', async ({uomCategoryId, uomCategory}: {uomCategoryId: string, uomCategory: UomCategoryModel}, {rejectWithValue}) => {
    try {
        const response =  await uomCategoryService.updateUomCategory(uomCategoryId, uomCategory);
        // @ts-ignore
        return {content: response.data.data.content, message: response.data.message}
    } catch (apiError) {
        const error = apiError as AxiosError<ErrorResponseModel>;
        if(error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if(error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue(UNKNOWN_ERROR);
        }
    }
});

export const createUomCategory = createAsyncThunk(
    'uomCategory/createUomCategory', async (uomCategory: UomCategoryModel, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.createUomCategory(uomCategory);
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

const uomCategorySlice = createSlice({
    name: "uomCategory",
    initialState: uomCategoryInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findUomCategoryById.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                uomCategoryAdapter.addOne(state, content);
            })
            .addCase(updateUomCategory.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.message = message;
                uomCategoryAdapter.updateOne(state, {id: content.id, changes: content});
            })
            .addCase(createUomCategory.fulfilled, (state, action) => {
                const {content, message} = action.payload;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                uomCategoryAdapter.addOne(state, content);
            })
            .addMatcher(
                isAnyOf(
                    findUomCategories.fulfilled,
                    searchUomCategories.fulfilled
                ), (state, action) => {
                    const {content, message} = action.payload;
                    console.log(action.payload);
                    state.loading = false;
                    state.pageSize = content.pageSize as number;
                    state.totalElements = content.totalElements as number;
                    state.totalPages = content.totalPages as number;
                    state.message = message;
                    uomCategoryAdapter.setAll(state, content.elements);
                })
            .addMatcher(
                isAnyOf(
                    findUomCategories.pending,
                    searchUomCategories.pending,
                    findUomCategoryById.pending,
                    updateUomCategory.pending,
                    createUomCategory.pending
                ), (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addMatcher(
                isAnyOf(
                    findUomCategories.rejected,
                    searchUomCategories.rejected,
                    findUomCategoryById.rejected,
                    updateUomCategory.rejected,
                    createUomCategory.rejected
                ), (state, action) => {
                    state.loading = false;
                    state.message = '';
                    state.error = action.payload as any;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.uomCategory.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.uomCategory.totalElements;
export const getTotalPages = (state: RootState) => state.inventory.uomCategory.totalPages;
export const getMessage = (state: RootState) => state.inventory.uomCategory.message;
export const getError = (state: RootState) => state.inventory.uomCategory.error;
export const getLoading = (state: RootState) => state.inventory.uomCategory.loading;

export const {
    selectAll: selectUomCategories,
    selectIds: selectUomCategoryIds,
    selectById: selectUomCategoryById,
    selectEntities: selectUomCategoryEntities,
} = uomCategoryAdapter.getSelectors((state: RootState) => state.inventory.uomCategory);

export default uomCategorySlice.reducer;