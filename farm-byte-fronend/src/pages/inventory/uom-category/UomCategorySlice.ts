import {createAsyncThunk, createEntityAdapter, createSelector, createSlice} from "@reduxjs/toolkit";
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
    'uomCategory/findUomCategories', async (findParam: FindParamModel) => {
        try {
            const response = await uomCategoryService.findUomCategories(findParam);
            return response.data;
    } catch (error) {
        console.log('Error', error);
    }

});

export const searchUomCategories = createAsyncThunk(
    'uomCategory/searchUomCategories', async (searchParam: SearchParamModel) => {
        let response = null;
        try {
            response = await uomCategoryService.searchUomCategories(searchParam);
            return response.data;
        } catch (error) {
            console.log('Error', error);
        }

    });


const uomCategorySlice = createSlice({
    name: "uomCategory",
    initialState: uomCategoryInitialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(findUomCategories.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(findUomCategories.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<UomCategoryModel>>;
                state.loading = false;
                state.pageSize = data.content.pageSize;
                state.totalElements = data.content.totalElements;
                state.totalPages = data.content.totalPages;
                state.message = message;
                uomCategoryAdapter.setAll(state, data.content.elements)
            })
            .addCase(findUomCategories.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
            .addCase(searchUomCategories.pending, (state) => {
                state.loading = true;
                state.message = '';
                state.error = null;
            })
            .addCase(searchUomCategories.fulfilled, (state, action) => {
                const {data, message} = action.payload as SuccessResponseModel<PageModel<UomCategoryModel>>;
                state.loading = false;
                state.pageSize = data.content.pageSize;
                state.totalElements = data.content.totalElements;
                state.totalPages = data.content.totalPages;
                state.message = message;
                uomCategoryAdapter.setAll(state, data.content.elements)
            })
            .addCase(searchUomCategories.rejected, (state, action) => {
                state.loading = false;
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