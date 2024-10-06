import {createAsyncThunk, createEntityAdapter, createSlice, isAnyOf} from "@reduxjs/toolkit";
import {UomCategoryModel} from "@/pages/inventory/uom-category/UomCategoryModel.ts";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/Store.ts";
import uomCategoryService from "@/pages/inventory/uom-category/UomCategoryService.ts";
import {DEFAULT_SIZE_VALUE} from "@/constants/page.constant.ts";
import {SearchParamModel} from "@/shared/model/searchParamModel.ts";


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
            return response.data;
        } catch (error) {
            if (error && error.response && error.response.data) {
                return rejectWithValue(error.response.data);
            } else if (error.message) {
                return rejectWithValue(error.message);
            } else {
                return rejectWithValue('Unknown error - Contact your administrator');
            }
        }
});

export const searchUomCategories = createAsyncThunk(
    'uomCategory/searchUomCategories', async (searchParam: SearchParamModel, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.searchUomCategories(searchParam);
            return response.data;
        } catch (error) {
            if (error && error.response && error.response.data) {
                return rejectWithValue(error.response.data);
            } else if (error.message) {
                return rejectWithValue(error.message);
            } else {
                return rejectWithValue('Unknown error - Contact your administrator');
            }
        }
});

export const findUomCategoryById = createAsyncThunk(
    'uomCategory/findUomCategoryById', async (uomCategoryId: string, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.findUomCategoryById(uomCategoryId);
            return response.data;
        } catch (error) {
            if (error && error.response && error.response.data) {
                return rejectWithValue(error.response.data);
            } else if (error.message) {
                return rejectWithValue(error.message);
            } else {
                return rejectWithValue('Unknown error - Contact your administrator');
            }
        }
});

export const updateUomCategory = createAsyncThunk(
    'uomCategory/updateUomCategory', async ({uomCategoryId, uomCategory}: {uomCategoryId: string, uomCategory: UomCategoryModel}, {rejectWithValue}) => {
    try {
        const response =  await uomCategoryService.updateUomCategory(uomCategoryId, uomCategory);
        return response.data;
    } catch (error) {
        if(error && error.response && error.response.data) {
            return rejectWithValue(error.response.data);
        } else if(error.message) {
            return rejectWithValue(error.message);
        } else {
            return rejectWithValue('Unknown error - Contact your administrator');
        }
    }
});

export const createUomCategory = createAsyncThunk(
    'uomCategory/createUomCategory', async (uomCategory: UomCategoryModel, {rejectWithValue}) => {
        try {
            const response =  await uomCategoryService.createUomCategory(uomCategory);
            return response.data;
        } catch (error) {
            if (error && error.response && error.response.data) {
                return rejectWithValue(error.response.data);
            } else if (error.message) {
                return rejectWithValue(error.message);
            } else {
                return rejectWithValue('Unknown error - Contact your administrator');
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
                const {data, message} = action.payload as SuccessResponseModel<UomCategoryModel>;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                uomCategoryAdapter.addOne(state, data.content);
            })
            .addCase(updateUomCategory.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<UomCategoryModel>;
                state.loading = false;
                state.message = message;
                uomCategoryAdapter.updateOne(state, {id: data.content.id, changes: data.content});
            })
            .addCase(createUomCategory.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<UomCategoryModel>;
                state.loading = false;
                state.totalElements = state.totalElements + 1;
                state.message = message;
                uomCategoryAdapter.addOne(state, data.content);
            })
            .addMatcher(
                isAnyOf(
                    findUomCategories.fulfilled,
                    searchUomCategories.fulfilled
                ), (state, action) => {
                    const {data, message} = action.payload as SuccessResponseModel<PageModel<UomCategoryModel>>;
                    state.loading = false;
                    state.totalElements = data.content.totalElements;
                    state.totalPages = data.content.totalPages;
                    state.message = message;
                    uomCategoryAdapter.setAll(state, data.content.elements);
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
                    state.error = action.payload;
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