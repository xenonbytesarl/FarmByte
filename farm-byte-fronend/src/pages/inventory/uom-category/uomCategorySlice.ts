import {createAsyncThunk, createEntityAdapter, createSlice, PayloadAction} from "@reduxjs/toolkit";
import {UomCategoryModel} from "@/pages/inventory/uom-category/uomCategoryModel.ts";
import axios from "axios";
import {SuccessResponseModel} from "@/shared/model/successResponseModel.ts";
import {PageModel} from "@/shared/model/pageModel.ts";
import {FindParamModel} from "@/shared/model/findParamModel.ts";
import {RootState} from "@/store/store.ts";
import {API_BASE_URL, API_JSON_HEADER} from "@/shared/constant/globalConstant.ts";


const uomCategoryAdapter = createEntityAdapter<UomCategoryModel>({
});

const uomCategoryInitialState = uomCategoryAdapter.getInitialState({
    loading: false,
    pageSize: 0,
    totalElements: 0,
    message: '',
    error: null
});


export const findUomCategories = createAsyncThunk('uomCategory/findUomCategories', async (findParam: FindParamModel): Promise<SuccessResponseModel<PageModel<UomCategoryModel>>> => {

    const response = await axios.get(
        API_BASE_URL + '/catalog/uom-categories',
        {
            params: {...findParam},
            headers: API_JSON_HEADER
        }
    );
    return response.data;
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
            .addCase(findUomCategories.fulfilled, (state, action: PayloadAction<SuccessResponseModel<PageModel<UomCategoryModel>>>) => {
                state.loading = false;
                state.pageSize = action.payload.data.content.pageSize;
                state.totalElements = action.payload.data.content.totalElements;
                state.message = action.payload.message;
                uomCategoryAdapter.upsertMany(state, action.payload.data.content.elements)
            })
            .addCase(findUomCategories.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload;
            })
    }
});

export const getPageSize = (state: RootState) => state.inventory.uomCategory.pageSize;
export const getTotalElements = (state: RootState) => state.inventory.uomCategory.totalElements;
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